package dom;

import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.*;
import java.io.File;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.ArrayList;
import javax.xml.transform.OutputKeys;

public class Dom {
    private static XPath xPath;
    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;
    
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java Dom <input.xml> <output.xml>");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputPath = args[1];
        
        initialize();
        transform(inputPath, outputPath);
    }
    
    private static void initialize() throws Exception {
        xPath = XPathFactory.newInstance().newXPath();
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }
    
    /**
     * Use this method to encapsulate the main logic for this example. First read in
     * the quiz document by using Dom.getUserList(Document). Second create
     * the output document by using Dom.createOutput(List<User>). Third use a Transformer
     * to print the document to the output path.
     * 
     * @param inputPath Path to the xml file to get read in.
     * @param outputPath Path to the xml file to print statistics.
     */
    private static void transform(String inputPath, String outputPath) throws Exception {
         // read XML file
    	Document quizDocument = documentBuilder.parse(inputPath);
	
	// get users
	List<User> users = getUserList(quizDocument);
	
	//create the output document
	Document outputDocument = createOutput(users);

	// define the output
    	TransformerFactory transformerFactory = TransformerFactory.newInstance();
    	Transformer transformer = transformerFactory.newTransformer();
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    	DOMSource source = new DOMSource(outputDocument);
    	StreamResult result = new StreamResult(new File(outputPath));

    	transformer.transform(source, result);
    }
    
    /**
     * Use this method to read in a List of User objects from the quiz document.
     * 
     * @param quizDocument Xml quiz document.
     * @return List of User objects read in from quizDocument.
     */
    private static List<User> getUserList(Document quizDocument) throws Exception {
	List<User> users = new ArrayList<User>();
	XPathExpression xpathExpr = xPath.compile("//user");
	NodeList userNodes = (NodeList)xpathExpr.evaluate(quizDocument, XPathConstants.NODESET);
		
	for (int i = 0; i < userNodes.getLength(); i++) {
		Node current = userNodes.item(i);
		NodeList childNodes = current.getChildNodes();		
		User user = new User();
		String name = current.getAttributes().getNamedItem("name").getTextContent();
		user.setName(name);
		user.setGender(current.getAttributes().getNamedItem("gender").getTextContent());
		user.setFullName(childNodes.item(3).getTextContent()+" "+childNodes.item(5).getTextContent());
		user.setBirthdate(childNodes.item(7).getTextContent());
		
		xpathExpr = xPath.compile("count(/quiz/games/game/round/answers/player[@ref='"+name+"']/answer)");
		Number answerCount = (Number) xpathExpr.evaluate(quizDocument, XPathConstants.NUMBER);		
		user.setAnswerCount(answerCount.intValue());
		
		xpathExpr = xPath.compile("count(/quiz/games/game/gamer[@ref='"+name+"'])");
		Number gameCount = (Number) xpathExpr.evaluate(quizDocument, XPathConstants.NUMBER);	
		user.setGameCount(gameCount.intValue());
		
		xpathExpr = xPath.compile("sum(/quiz/games/game/round/answers/player[@ref='"+name+"']/answer/@time)");
		Number sumTime = (Number) xpathExpr.evaluate(quizDocument, XPathConstants.NUMBER);	
		user.setSumTime(sumTime.intValue());

		users.add(user);
	}
	return users;
    }
    
    /**
     * Use this method to create the output xml file from a List of User objects.
     * 
     * @param users List of User objects previously read in via Dom.getUserList(Document)
     * @return Xml document which encapsulates the data as required by the task description.
     */
    private static Document createOutput(List<User> users) {
        Document outputDocument = documentBuilder.newDocument();
	Element rootElement = outputDocument.createElement("users-dom");	
	outputDocument.appendChild(rootElement);
	for (User user : users) {
		Element userElement = outputDocument.createElement("user");
		userElement.setAttribute("birthdate", user.getBirthdate());
		userElement.setAttribute("fullname", user.getFullName());
		userElement.setAttribute("gender", user.getGender());
		userElement.setAttribute("name", user.getName());

		Element answerCountElement = outputDocument.createElement("answerCount");
		answerCountElement.setTextContent(""+user.getAnswerCount());
		userElement.appendChild(answerCountElement);
		
     		Element gameCountElement = outputDocument.createElement("gameCount");
		gameCountElement.setTextContent(""+user.getGameCount());
		userElement.appendChild(gameCountElement);
		
      		Element sumTimeElement = outputDocument.createElement("sumTime");		
		sumTimeElement.setTextContent(""+user.getSumTime());
		userElement.appendChild(sumTimeElement);

		rootElement.appendChild(userElement);
	}	

	return outputDocument;
    }
}
