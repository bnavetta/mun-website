package org.brownmun.cli.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.google.common.io.Files;

import org.brownmun.core.committee.CommitteeService;
import org.brownmun.core.committee.model.Committee;
import org.brownmun.core.committee.model.Position;

@ShellComponent
public class CommitteeCommands
{
    private final CommitteeService committees;

    @Autowired
    public CommitteeCommands(CommitteeService committees)
    {
        this.committees = committees;
    }

    /**
     * This command is for bulk-creating committee positions. It uses a file format
     * designed for easy copy-and-pasting from background guides and other
     * documents.
     *
     * The input file consists of groups, one per committee. Each group starts with
     * a header indicating the committee by its short name. After that, each line
     * contains a position name:
     *
     * <pre>
     * {@code
     *     <ecosoc>
     *     United States
     *     Canada
     *     China
     *     South Africa
     *     <adhoc>
     *     Some Position
     *     Another Position
     * }
     * </pre>
     *
     * @param input file to read positions from
     */
    @ShellMethod("Create committee positions from an input file")
    public void createPositions(File input) throws IOException
    {
        Pattern header = Pattern.compile("<(\\w+)>");

        try (BufferedReader src = Files.newReader(input, StandardCharsets.UTF_8))
        {
            String line;
            Committee committee = null;
            Set<String> positionNames = null;
            while ((line = src.readLine()) != null)
            {
                Matcher headerMatch = header.matcher(line);
                if (headerMatch.matches())
                {
                    String committeeSlug = headerMatch.group(1);
                    committee = committees.findByShortName(committeeSlug)
                            .orElseThrow(() -> new IllegalArgumentException("Committee not found: " + committeeSlug));
                    positionNames = committees.getPositions(committee).stream().map(Position::getName).collect(
                            Collectors.toSet());
                    System.out.printf("\n\nCreating positions for %s\n", committee.getName());
                }
                else if (committee == null)
                {
                    throw new IllegalStateException("Missing committee header");
                }
                else
                {
                    if (positionNames.contains(line))
                    {
                        System.out.printf("%s already exists\n", line);
                        continue;
                    }

                    Position pos = new Position();
                    pos.setCommittee(committee);
                    pos.setName(line);
                    pos = committees.savePosition(pos);
                    System.out.printf("Created %s (%d)\n", pos.getName(), pos.getId());
                }
            }
        }
    }
}
