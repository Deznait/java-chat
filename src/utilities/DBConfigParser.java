package utilities;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;


public class DBConfigParser {
	private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    
    
    /**
     * Parses the database configuration from the specified XML file.
     *
     * @param configFile the XML configuration file
     * @throws Exception if there's an error parsing the configuration
     */
    public void parseConfig(String configFile) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        DefaultHandler handler = new DefaultHandler() {
            private String currentElement = "";

            @Override
            public void startElement(String uri, String localName, 
                                     String qName, Attributes attributes) {
                currentElement = qName;
            }

            @Override
            public void characters(char[] ch, int start, int length) {
                String value = new String(ch, start, length).trim();
                if (!value.isEmpty()) {
                    switch (currentElement) {
                        case "host":
                        	host = value;
                            break;
                        case "port":
                        	port = value;
                            break;
                        case "database":
                        	database = value;
                            break;
                        case "username":
                            username = value;
                            break;
                        case "password":
                            password = value;
                            break;
                    }
                }
            }
        };

        saxParser.parse(new File(configFile), handler);
    }


    /**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
}
