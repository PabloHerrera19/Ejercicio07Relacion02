import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PrincipalEj07R02 {

	private static final String FICHERO_ORIGINAL = "resultados.xml";
	private static final String FICHERO_RESULTADO = "resultadosNuevo.xml";

	public static void main(String[] args) {

		/**
		 * Se dispone del fichero“​resultados.xm​l”con los datos de los resultados del
		 * partido de la última jornada de liga. Usando DOM y Transformer construir un
		 * nuevo fichero xml llamado “resultadosNuevo.xml”​ donde se hagan el siguiente
		 * cambio sobre el fichero original​ :◦Crear un atributo en el elemento partido
		 * con el nombre ​resultadoQuiniela con los valores ‘1’, ‘X’, ‘2’
		 */

		try {

			Document arbolDocumento = crearArbolDOM();
			modificarXml(arbolDocumento);
			transformarDOMEnXml(arbolDocumento);
			System.out.println("Documento " + FICHERO_RESULTADO + " creado correctamente.");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private static void modificarXml(Document arbolDocumento) {

		// Variables locales al método
		String resultadoQuiniela;
		Element raiz = (Element) arbolDocumento.getFirstChild();
		NodeList listaPartidos = raiz.getElementsByTagName("partido");
		Element partido;
		
		for (int contadorPartidos = 0; contadorPartidos < listaPartidos.getLength(); contadorPartidos++) {
			partido = (Element) listaPartidos.item(contadorPartidos);
			resultadoQuiniela = comprobarResultado(partido);
			addResultado(partido, resultadoQuiniela);
//			addHijo(arbolDocumento, partido, resultadoQuiniela);
			System.out.println("partido " + contadorPartidos + " realizado");
		}
		

	}

	private static void addHijo(Document arbolDocumento, Element partido, String resultadoQuiniela) {
		
		Element hijo;
		hijo = arbolDocumento.createElement("resultadoQuiniela");
		hijo.setTextContent(resultadoQuiniela);
		partido.appendChild(hijo);
		
	}

	private static void addResultado(Element partido, String resultadoQuiniela) {
		
		if (!(partido.hasAttribute("resultadoQuiniela"))) {
			partido.setAttribute("resultadoQuiniela", resultadoQuiniela);
		}
	}

	private static String comprobarResultado(Element partido) {

		// Variables locales al método
		int golesLocal, golesVisitante;
		String resultado;
		NodeList listaGoles = partido.getElementsByTagName("goles");
		
		golesLocal = Integer.parseInt(listaGoles.item(0).getTextContent());
		golesVisitante = Integer.parseInt(listaGoles.item(1).getTextContent());

		if (golesLocal > golesVisitante) {
			resultado = "1";
		} else if (golesLocal < golesVisitante) {
			resultado = "2";

		} else {
			resultado = "X";
		}

		return resultado;
	}


	private static void transformarDOMEnXml(Document arbolDocumento)
			throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		Source source = new DOMSource(arbolDocumento);
		Result result = new StreamResult(FICHERO_RESULTADO);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(source, result);
	}

	private static Document crearArbolDOM() throws ParserConfigurationException, SAXException, IOException {
		// Extraemos el arbol de nuestro documento XML.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document arbolDocumento = builder.parse(new File(FICHERO_ORIGINAL));
		return arbolDocumento;
	}

}
