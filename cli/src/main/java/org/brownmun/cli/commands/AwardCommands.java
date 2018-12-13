package org.brownmun.cli.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Strings;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import de.vandermeer.asciitable.AsciiTable;
import org.brownmun.cli.awards.AwardsGenerator;
import org.brownmun.core.award.AwardExport;
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
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public void generateAwards(File output, @ShellOption(defaultValue = "") String only) throws IOException
    {
        AwardsGenerator gen = new AwardsGenerator(new File("config/awards.docx"));

        AwardExport allAwards = awardService.exportAwards();
        List<AwardPrint> toGenerate = allAwards.getCompleteAwards();
        if (!Strings.isNullOrEmpty(only))
        {
            toGenerate = filterFromFile(toGenerate, new File(only));
        }

        Map<AwardType, List<AwardPrint>> awards = toGenerate
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

        CsvSchema schema = mapper.schemaFor(AwardPrint.class).withHeader();
        ObjectWriter writer = mapper.writerFor(AwardPrint.class).with(schema);
        try (SequenceWriter out = writer.writeValues(new File(output, "incomplete.csv"))) {
            out.writeAll(allAwards.getIncompleteAwards());
            out.flush();
        }
    }

    @ShellMethod("Find incomplete awards")
    public void incompleteAwards()
    {
        String format = " %4s | %20s | %50s | %40s | %40s\n";

        System.out.printf(format, "ID", "Type", "Committee", "School", "Delegate");

        for (AwardPrint award : awardService.exportAwards().getIncompleteAwards())
        {
            System.out.printf(format, award.getId(), award.getType().getDisplayName(), award.getCommitteeName(), award.getSchoolName(), award.getDelegateName());
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

    @ShellMethod("Export an awards spreadsheet")
    public void exportAwards(File outputFile) throws IOException
    {
        AwardExport export = awardService.exportAwards();

        CsvSchema schema = mapper.schemaFor(AwardPrint.class).withHeader();
        ObjectWriter writer = mapper.writerFor(AwardPrint.class).with(schema);
        try (OutputStream out = new FileOutputStream(outputFile))
        {
            // Write a UTF-8 BOM because Excel expects it :(
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            try (SequenceWriter s = writer.writeValues(out))
            {
                s.writeAll(export.getCompleteAwards());
                s.flush();
            }
        }

        for (AwardPrint incomplete : export.getIncompleteAwards())
        {
            System.out.println("Incomplete award: " + incomplete);
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

    private List<AwardPrint> filterFromFile(List<AwardPrint> awards, File filterFile) throws IOException
    {
        CsvSchema schema = mapper.schemaFor(AwardPrint.class).withHeader();
        ObjectReader reader = mapper.readerFor(AwardPrint.class).with(schema);
        try (MappingIterator<AwardPrint> filterIter = reader.readValues(filterFile))
        {
            Set<Long> toFilter = filterIter.readAll().stream()
                    .map(AwardPrint::getId).collect(Collectors.toSet());

            return awards.stream().filter(a -> toFilter.contains(a.getId())).collect(Collectors.toList());
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
