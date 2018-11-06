package org.brownmun.cli.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.brownmun.core.award.AwardService;
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
