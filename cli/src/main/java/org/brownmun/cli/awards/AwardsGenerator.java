package org.brownmun.cli.awards;

import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.brownmun.core.award.model.AwardPrint;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

import java.io.*;
import java.util.List;

public class AwardsGenerator
{
    private final File templateFile;

    public AwardsGenerator(File templateFile) {
        this.templateFile = templateFile;
    }

    public void writeAwards(List<AwardPrint> awards, File out) throws IOException
    {
        XWPFDocument doc = createAward(awards.get(0));
        for (int i = 1; i < awards.size(); i++)
        {
            XWPFDocument award = createAward(awards.get(i));
            doc.getDocument().addNewBody().set(award.getDocument().getBody());
        }

        try (OutputStream os = new FileOutputStream(out))
        {
            doc.write(os);
        }
    }

    public void writeAward(AwardPrint award, File out) throws IOException
    {
        XWPFDocument doc = createAward(award);
        try (OutputStream os = new FileOutputStream(out))
        {
            doc.write(os);
        }
    }

    private XWPFDocument createAward(AwardPrint award) throws IOException {

        XWPFDocument doc = readTemplate();

        for (XWPFParagraph p : doc.getParagraphs())
        {
            List<XWPFRun> runs = p.getRuns();
            if (runs == null)
            {
                continue;
            }

            for (XWPFRun run : runs)
            {
                String text = run.text();
                text = text.replace("$AWARD$", award.getType().getDisplayName());
                text = text.replace("$DELEGATE$", award.getDelegateName());
                text = text.replace("$SCHOOL$", award.getSchoolName());
                text = text.replace("$POSITION$", award.getPositionName());
                text = text.replace("$COMMITTEE$", award.getCommitteeName());
                run.setText(text, 0);
            }
        }

        return doc;
    }

    private XWPFDocument readTemplate() throws IOException
    {
        try (InputStream in = new FileInputStream((templateFile)))
        {
            return new XWPFDocument(in);
        }
    }
}
