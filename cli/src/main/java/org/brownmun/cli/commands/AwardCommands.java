package org.brownmun.cli.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import de.vandermeer.asciitable.AsciiTable;
import org.brownmun.cli.awards.AwardsGenerator;
import org.brownmun.core.award.AwardService;
import org.brownmun.core.award.model.Award;
import org.brownmun.core.award.model.AwardPrint;
import org.brownmun.core.award.model.AwardType;
import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.committee.model.Committee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ShellComponent
public class AwardCommands
{
    private static final Logger log = LoggerFactory.getLogger(AwardCommands.class);

    private final AwardService awardService;
    private final CommitteeService committeeService;
    private final CsvMapper mapper = new CsvMapper();

    @Autowired
    public AwardCommands(AwardService awardService, CommitteeService committeeService)
    {
        this.awardService = awardService;
        this.committeeService = committeeService;
    }

    @ShellMethod("Generate award files")
    public void generateAwards(File output) throws IOException
    {
        AwardsGenerator gen = new AwardsGenerator(new File("config/awards.docx"));

        Map<AwardType, List<AwardPrint>> awards = awardService.exportAwards()
                .stream()
                .collect(Collectors.groupingBy(AwardPrint::getType));

        if (output.exists())
        {
            MoreFiles.deleteRecursively(output.toPath(), RecursiveDeleteOption.ALLOW_INSECURE);
        }
        if (!output.mkdir())
        {
            System.err.println("Couldn't create directory " + output);
            return;
        }

        for (var awardSet : awards.entrySet())
        {
            if (awardSet.getValue().isEmpty())
            {
                continue;
            }
            File outputFile = new File(output, awardSet.getKey().toString() + ".docx");
            gen.writeAwards(awardSet.getValue(), outputFile);
        }
    }

    @ShellMethod("Find unassigned awards")
    public String unassignedAwards()
    {
        AsciiTable table = new AsciiTable();
        table.addRow("ID", "Type", "Committee", "Committee ID");
        table.addRule();

        for (Award award : awardService.findUnassignedAwards())
        {
            table.addRow(award.getId(), award.getType().getDisplayName(), award.getCommittee().getId(), award.getCommittee().getName());
        }

        return table.render(128);
    }

    @ShellMethod("Export all assigned awards")
    public void exportAwards(File outputFile) throws IOException
    {
        CsvSchema schema = mapper.schemaFor(AwardPrint.class).withHeader();
        ObjectWriter writer = mapper.writerFor(AwardPrint.class).with(schema);
        try (SequenceWriter out = writer.writeValues(outputFile))
        {
            out.writeAll(awardService.exportAwards());
            out.flush();
        }
    }

    @ShellMethod("Create awards from the awards allocation spreadsheet")
    public void createAwards(File inputFile) throws IOException {
        awardService.clearAwards();

        CsvSchema schema = mapper.schemaFor(AwardsAllocation.class).withHeader();
        ObjectReader reader = mapper.readerFor(AwardsAllocation.class).with(schema);
        try (MappingIterator<AwardsAllocation> allocationIter = reader.readValues(inputFile))
        {
            while (allocationIter.hasNextValue())
            {
                AwardsAllocation allocation = allocationIter.nextValue();

                log.info("Creating awards for {} ({})", allocation.committeeName, allocation.committeeId);
                Committee committee = committeeService.getCommittee(allocation.committeeId).get();

                for (int i = 0; i < allocation.bestDelegateCount; i++)
                {
                    awardService.createAward(committee, AwardType.BEST_DELEGATE);
                }

                for (int i = 0; i < allocation.outstandingDelegateCount; i++)
                {
                    awardService.createAward(committee, AwardType.OUTSTANDING_DELEGATE);
                }

                for (int i = 0; i < allocation.honorableDelegateCount; i++)
                {
                    awardService.createAward(committee, AwardType.HONORABLE_DELEGATE);
                }
            }
        }
    }

    @JsonPropertyOrder({"ID", "Committee", "Best Delegate", "Outstanding Delegate", "Honorable Delegate"})
    private static class AwardsAllocation {
        @JsonProperty("ID")
        public long committeeId;

        @JsonProperty("Committee")
        public String committeeName;

        @JsonProperty("Best Delegate")
        public int bestDelegateCount;

        @JsonProperty("Outstanding Delegate")
        public int outstandingDelegateCount;

        @JsonProperty("Honorable Delegate")
        public int honorableDelegateCount;
    }
}