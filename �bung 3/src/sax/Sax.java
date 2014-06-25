package sax;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import org.xml.sax.helpers.DefaultHandler;
import java.io.FileReader;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class Sax {

    /**
     * The PrintWriter's append() function can be used to write the
     * SQL-statements into the output file.
     */
    private static PrintWriter printWriter;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java Sax <input file> <output file>");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputPath = args[1];

        createInsertSQL(inputPath, outputPath);
    }

    /**
     * @param inputPath Path to an input .xml file.
     * @param outputPath Path of an output .sql file.
     * @throws Exception
     */
    private static void createInsertSQL(String inputPath, String outputPath) throws Exception {
        // you can use the printWriter to write your generated SQL into the output file
        printWriter = new PrintWriter(new FileOutputStream(outputPath));
	
	//parse XML file and set contentHanlder
	XMLReader xr = XMLReaderFactory.createXMLReader();	
	xr.setContentHandler(new SQLContentHandler());	
	FileReader r = new FileReader(inputPath);
	xr.parse(new InputSource(r));

        // close stream
        printWriter.close();
    }

    public static class SQLContentHandler extends DefaultHandler {
	private StringBuffer buffer = new StringBuffer();	
	private int id; 
	private String text;
	private int nrOfChoices;
	private int nrOfCorrectChoices;
	private int nrOfGroups;

	public void startElement (String uri, String localName, String qName, Attributes attributes){
		if(localName.equals("question")){
			id = Integer.parseInt(attributes.getValue("id"));
			nrOfChoices=0;
			nrOfCorrectChoices=0;
			nrOfGroups=0;
		}
		if(localName.equals("text")){
			buffer = new StringBuffer();		
		}
		if(localName.equals("choice")){
			if(attributes.getValue("correct")!=null && attributes.getValue("correct").equals("true")){
				nrOfCorrectChoices++;
			}			
			nrOfChoices++;
		}
		if(localName.equals("group")){
			nrOfGroups++;		
		}	
	}

	public void characters(char[] ch, int start, int length){
		buffer.append(new String(ch,start,length));	
	}

    	public void endElement(String uri, String localName, String qName){
		if(localName.equals("text")){
			text = "'" +buffer.toString()+"'";
		}		
		else if (localName.equals("question")){	 
			printWriter.append("INSERT INTO Questions (id, text, nrOfChoices, nrOfCorrectChoices, nrOfGroups) values (" + id +", " + text + ", "+ nrOfChoices + ", " +nrOfCorrectChoices + ", " + nrOfGroups + ")" + '\n');
		}
    	}
    }
}
