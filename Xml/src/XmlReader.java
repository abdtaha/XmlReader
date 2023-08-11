import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileWriter;
import java.io.IOException;

public class XmlReader {
    public static void main(String[] args) {
        try {
            // Skapa en SAXParserFactory och en SAXParser
            SAXParserFactory f = SAXParserFactory.newInstance();
            SAXParser saxP = f.newSAXParser();
            
            XmlHandler handler = new XmlHandler();
            
            // Parsa XML-filen med hanteraren
            saxP.parse("sma_gentext.xml", handler);
            
            // Hämta och spara målvärdet om det hittades
            if (handler.getTargetValue() != null) {
                writeToFile(handler.getTargetValue(), "New Text Document.txt");
                System.out.println("Värdet har sparats i New Text Document.txt.");
            } else {
                System.out.println("Inget matchande värde hittades.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metod för att skriva till en fil
    private static void writeToFile(String content, String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            System.err.println("Kunde inte skriva till filen: " + e.getMessage());        }
    }
}

// En hanterare för SAX-parseren
class XmlHandler extends DefaultHandler {
    private String currentValue;  // lagrar  aktuella elementets värde
    private String targetValue;   // Målvärdet för id 42007
    private boolean found;      // Har värdet true om vi har hittat önskade id

    //  Hämtar målvärdet
    public String getTargetValue() {
        return targetValue;
    }

    // Kallas när ett nytt element startar
    @Override
    public void startElement(String s, String lName, String qName, Attributes a) throws SAXException {
        if (qName.equals("trans-unit")) {
            String id = a.getValue("id");
            if (id != null && id.equals("42007")) {
                found = true; 
            } else {
                found = false;
            }
        }
    }

    // Kallas när tecken inuti ett element hittas
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (found) {
            currentValue = new String(ch, start, length);  
        }
    }

    // Kallas när ett element avslutas
    @Override
    public void endElement(String s, String lName, String qName) throws SAXException {
        if (qName.equals("target") && found) {
            targetValue = currentValue;  
        }
    }
}

