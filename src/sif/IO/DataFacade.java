package sif.IO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Workbook;

import sif.IO.spreadsheet.IPOIOutput;
import sif.IO.spreadsheet.ISpreadsheetIO;
import sif.IO.spreadsheet.InvalidSpreadsheetFileException;
import sif.IO.spreadsheet.poi.EPOISpreadsheetType;
import sif.IO.spreadsheet.poi.POIOutput;
import sif.IO.spreadsheet.poi.POISpreadsheetIO_HSSF;
import sif.IO.spreadsheet.poi.POISpreadsheetIO_XSSF;
import sif.model.elements.basic.spreadsheet.Spreadsheet;
import sif.model.elements.containers.AbstractElementList;
import sif.model.elements.custom.InputCell;
import sif.model.elements.custom.IntermediateCell;
import sif.model.elements.custom.OutputCell;
import sif.model.inspection.InspectionRequest;
import sif.model.violations.Findings;
import sif.model.violations.ISingleViolation;
import sif.model.violations.IViolation;
import sif.model.violations.IViolationGroup;
import sif.model.violations.lists.ViolationList;

/***
 * DataFacade provides access to IO functions. It can create a
 * {@link Spreadsheet} from a given spreadsheet file and create a html-report
 * for a given inspection request. Implements the Facade and Singleton pattern.
 * 
 * 
 * @author Sebastian Zitzelsberger, Manuel Lemcke, Ehssan Doust
 * 
 */
public final class DataFacade {

	private static DataFacade theInstance;

	/***
	 * Gets the instance of the DataFacade.
	 * 
	 * @return The instance.
	 */
	public static DataFacade getInstance() {
		if (theInstance == null) {
			theInstance = new DataFacade();
		}
		return theInstance;
	}

	// /**
	// * Creates a POI Workbook and writes it as a File. Depending on the file
	// * extension it's a HSSF (for .xls) or a XSSF (else) Workbook.
	// *
	// * @param spreadsheet
	// * @param filename
	// * @return
	// */
	// public boolean createAndWriteWorkbook(Spreadsheet spreadsheet,
	// String filename) {
	// Workbook workbook;
	// if (filename.endsWith("xls")) {
	// workbook = createHSSFWorkbook(spreadsheet);
	// } else {
	// workbook = createXSSFWorkbook(spreadsheet);
	// }
	// return writePOIWorkbook(workbook, filename);
	// }

	/**
	 * Creates a POI XSSF Workbook from a SIF Spreadsheet
	 * 
	 * @param spreadsheet
	 *            The SIF {@link Spreadsheet}
	 * @return A XSSF {@link Workbook}
	 */
	public Workbook createXSSFWorkbook(Spreadsheet spreadsheet) {
		IPOIOutput poiOutput = new POIOutput(EPOISpreadsheetType.XSSF);
		Workbook result = poiOutput.createPOIWorkbook(spreadsheet);

		return result;
	}

	/**
	 * Creates a POI HSSF Workbook from a SIF Spreadsheet
	 * 
	 * @param spreadsheet
	 *            The SIF {@link Spreadsheet}
	 * @return A HSSF {@link Workbook}
	 */
	public Workbook createHSSFWorkbook(Spreadsheet spreadsheet) {
		IPOIOutput poiOutput = new POIOutput(EPOISpreadsheetType.HSSF);
		Workbook result = poiOutput.createPOIWorkbook(spreadsheet);
		return result;
	}

	// /**
	// * Writes a POI Workbook to a file
	// *
	// * @param workbook
	// * @return
	// */
	// public boolean writePOIWorkbook(Workbook workbook, String filename) {
	// boolean result = false;
	// FileOutputStream fileOut;
	//
	// try {
	// fileOut = new FileOutputStream(filename);
	// workbook.write(fileOut);
	// fileOut.close();
	// result = true;
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// return result;
	// }

	/***
	 * 
	 * Calls the corresponding {@link ISpreadsheetIO} to create a
	 * {@link Spreadsheet} from the given spreadsheet file.
	 * 
	 * @param spreadsheetFile
	 *            The given spreadsheet file.
	 * @return The spreadsheet for the given spreadsheet spreadsheet file.
	 * @throws Exception
	 */
	public Spreadsheet createSpreadsheet(File spreadsheetFile) throws Exception {
		ISpreadsheetIO spreadsheetIO = null;

		spreadsheetIO = createSpreadsheetIO(spreadsheetFile.getName());

		return spreadsheetIO.createSpreadsheet(spreadsheetFile);
	}

	public Spreadsheet createSpreadsheet(Workbook workbook, String fileName)
			throws Exception {
		ISpreadsheetIO spreadsheetIO = null;
		String[] fileNameParts;
		String name = "";
		Spreadsheet spreadsheet = null;

		spreadsheetIO = createSpreadsheetIO(fileName);

		fileNameParts = fileName.split("\\.");
		if (fileNameParts.length > 1) {
			name = fileNameParts[fileNameParts.length - 2];
		} else {
			name = fileName;
		}

		// if (fileName.endsWith(".xlsx")) {
		// name = fileName.replace(".xlsx", "");
		// }
		// else if (fileName.endsWith(".xls")) {
		// name = fileName.replace(".xls", "");
		// }
		// else {
		// throw new Exception("Invalid Filename");
		// }

		spreadsheet = spreadsheetIO.createSpreadsheet(workbook, name);

		return spreadsheet;
	}

	public ISpreadsheetIO createSpreadsheetIO(String fileExtension)
			throws InvalidSpreadsheetFileException {
		ISpreadsheetIO spreadsheetIO = null;

		// .xls Files are handled by the POISpreadsheetIO_HSSF;
		if (fileExtension.endsWith("xls")) {
			spreadsheetIO = new POISpreadsheetIO_HSSF();
			// .xlsx files are handled by the POISpreadsheetIO_XSSF;
		} else if (fileExtension.endsWith("xlsx")) {
			spreadsheetIO = new POISpreadsheetIO_XSSF();
			// Other files are not supported yet.
		} else {
			throw new InvalidSpreadsheetFileException(
					"Unsupported spreadsheet file format.");
		}

		return spreadsheetIO;
	}

	/***
	 * Creates a report for the given inspection request into the given file,
	 * using the request's name as a filename.
	 * 
	 * @param inspectionRequest
	 *            The given inspection request.
	 * @param file
	 *            The given file.
	 * @throws IOException
	 *             Throws an IOExcpetion if the file can't be created at the
	 *             given path.
	 */
	public void export(InspectionRequest inspectionRequest, File file,
			ReportFormat format) throws IOException {

		String content = null;

		switch (format) {
		case HTML:
			content = getFindingsAsHtmlString(inspectionRequest);
			break;
		case XML:
			content = getFindingsAsXmlString(inspectionRequest);
			break;
		}

		FileWriter outFile = new FileWriter(file);
		PrintWriter out = new PrintWriter(outFile);
		out.append(content);
		out.close();
	}

	/***
	 * Creates a report for the given inspection request at the given path,
	 * using the request's name as a filename.
	 * 
	 * @param inspectionRequest
	 *            The given inspection request.
	 * @param path
	 *            The given path.
	 * @throws IOException
	 *             Throws an IOExcpetion if the file can't be created at the
	 *             given path.
	 */
	public void export(InspectionRequest inspectionRequest, String path,
			ReportFormat format) throws IOException {
		String filePath = path;
		if (!filePath.endsWith(File.pathSeparator)) {
			filePath = filePath + File.separator;
		}

		String extension = null;

		switch (format) {
		case HTML:
			extension = ".html";
			break;
		case XML:
			extension = ".xml";
			break;
		}

		File file = new File(filePath + inspectionRequest.getName() + extension);
		this.export(inspectionRequest, file, format);
	}

	/***
	 * Creates a report for the given inspection request
	 * 
	 * @param inspectionRequest
	 *            The given inspection request.
	 * @throws Exception 
	 * @throws IOException
	 *             Throws an IOExcpetion if the file can't be created at the
	 *             given path.
	 */
	public String export(InspectionRequest inspectionRequest,
			ReportFormat format) throws Exception {

		switch (format) {
		case HTML:
			return this.getFindingsAsHtmlString(inspectionRequest);
		case XML:
			return this.getFindingsAsJAXBString(inspectionRequest);
		default:
			return null;
		}

	}

	/**
	 * @deprecated handwritten xml; use {@link #getFindingsAsJAXBString(InspectionRequest)} instead
	 * @param inspectionRequest
	 * @return
	 */
	private String getFindingsAsXmlString(InspectionRequest inspectionRequest) {
		StringBuilder builder = new StringBuilder();
		Findings findings = inspectionRequest.getFindings();
		builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		builder.append("<test title=\""
				+ inspectionRequest.getSpreadsheetFile().getName()
				+ "\" file=\""
				+ inspectionRequest.getInventory().getSpreadsheetFile()
						.getAbsolutePath() + "\">\n");

		builder.append("<policy author=\""
				+ inspectionRequest.getPolicy().getAuthor()
				+ "\" description=\""
				+ inspectionRequest.getPolicy().getDescription() + "\" name=\""
				+ inspectionRequest.getPolicy().getName() + "\" />\n");

		// Write Input-, Intermediate & Outputcells
		builder.append("<cells>\n");

		builder.append("<input>\n");

		AbstractElementList<InputCell> inputCells = inspectionRequest
				.getInventory().getListFor(InputCell.class);
		for (int i = 0; i < inputCells.getNumberOfElements(); i++) {
			InputCell currentCell = inputCells.getElements().get(i);
			builder.append("<cell number=\"" + i + "\" location=\""
					+ currentCell.getLocation() + "\" content=\""
					+ currentCell.getStringRepresentation() + "\" />\n");
		}

		builder.append("</input>\n");

		builder.append("<intermediate>\n");

		AbstractElementList<IntermediateCell> intermediateCells = inspectionRequest
				.getInventory().getListFor(IntermediateCell.class);
		for (int i = 0; i < intermediateCells.getNumberOfElements(); i++) {
			IntermediateCell currentCell = intermediateCells.getElements().get(
					i);
			builder.append("<cell number=\"" + i + "\" location=\""
					+ currentCell.getLocation() + "\" content=\""
					+ currentCell.getStringRepresentation() + "\" />\n");
		}

		builder.append("</intermediate>\n");

		builder.append("<output>\n");

		AbstractElementList<OutputCell> outputCells = inspectionRequest
				.getInventory().getListFor(OutputCell.class);
		for (int i = 0; i < outputCells.getNumberOfElements(); i++) {
			OutputCell currentCell = outputCells.getElements().get(i);
			builder.append("<cell number=\"" + i + "\" location=\""
					+ currentCell.getLocation() + "\" content=\""
					+ currentCell.getStringRepresentation() + "\" />\n");
		}

		builder.append("</output>\n");

		builder.append("</cells>\n");

		// Write Findings
		builder.append("<findings>\n");

		for (ViolationList violationsList : findings.getViolationLists()) {

			builder.append("<rule name=\""
					+ StringEscapeUtils.escapeXml(violationsList.getPolicyRule().getName())
					+ "\" author=\""
					+ StringEscapeUtils.escapeXml(violationsList.getPolicyRule().getAuthor())
					+ "\" description=\""
					+ StringEscapeUtils.escapeXml(violationsList.getPolicyRule().getDescription())
							.replace('"', '\'') + "\" background=\""
					+ StringEscapeUtils.escapeXml(violationsList.getPolicyRule().getBackground())
					+ "\" severity=\""
					+ violationsList.getPolicyRule().getSeverityWeight() // nn escaping a double
					+ "\" solution=\""
					+ StringEscapeUtils.escapeXml(violationsList.getPolicyRule().getPossibleSolution())
					+ "\" type=\""
					+ StringEscapeUtils.escapeXml(violationsList.getPolicyRule().getType().toString())
					+ "\">\n");

			Integer counter = 0;
			for (IViolation violation : violationsList.getViolations()) {
				// Write a single violation.
				if (violation instanceof ISingleViolation) {
					ISingleViolation singleViolation = (ISingleViolation) violation;
					counter++;

					String causingElement = "unknown";
					String location = "unknown";

					if (singleViolation.getCausingElement() != null) {
						causingElement = StringEscapeUtils.escapeXml
								(singleViolation.getCausingElement().getStringRepresentation());
						location = singleViolation.getCausingElement()
								.getLocation();
					}

					builder.append("<singleviolation number=\"" + counter
							+ "\" causingelement=\"" 
							+ StringEscapeUtils.escapeXml(causingElement)
							+ "\" location=\"" 
							+ StringEscapeUtils.escapeXml(location) 
							+ "\" description=\""
							+ StringEscapeUtils.escapeXml(singleViolation.getDescription())
							+ "\" severity=\""
							+ singleViolation.getWeightedSeverityValue()
							+ "\" />\n");

				} else {
					// Write a violation group;
					IViolationGroup violationGroup = (IViolationGroup) violation;

					String causingElement = "unknown";
					String location = "unknown";

					if (violationGroup.getCausingElement() != null) {
						causingElement = violationGroup.getCausingElement()
								.getStringRepresentation();
						location = violationGroup.getCausingElement()
								.getLocation();
					}
					counter++;

					builder.append("<violationgroup number=\"" + counter
							+ "\" causingelement=\"" + causingElement
							+ "\" location=\"" + location + "\" description=\""
							+ violationGroup.getDescription()
							+ "\" severity=\""
							+ violationGroup.getWeightedSeverityValue()
							+ "\">\n");

					Integer groupCounter = 0;
					for (ISingleViolation singleViolation : violationGroup
							.getMembers()) {

						groupCounter++;

						String innerCausingElement = "unknown";
						String innerLocation = "unknown";

						if (singleViolation.getCausingElement() != null) {
							innerCausingElement = singleViolation
									.getCausingElement()
									.getStringRepresentation();
							innerLocation = singleViolation.getCausingElement()
									.getLocation();
						}

						builder.append("<singleviolation number=\"" + counter
								+ "." + groupCounter + "\" causingelement=\""
								+ innerCausingElement + "\" location=\""
								+ innerLocation + "\" description=\""
								+ singleViolation.getDescription()
								+ "\" severity=\""
								+ singleViolation.getWeightedSeverityValue()
								+ "\" />\n");

					}

					builder.append("</violationgroup>\n");
				}
			}

			builder.append("</rule>\n");

		}

		builder.append("</findings>\n");

		builder.append("</test>\n");

		return builder.toString();
	}
	
	private String getFindingsAsJAXBString(InspectionRequest inspectionRequest) throws Exception{
		JAXBContext jc = JAXBContext.newInstance("sif.model.policy"
				+ ":sif.model.inspection"
				+ ":sif.model.violations"
				+ ":sif.model.violations.single"
				+ ":sif.model.violations.groups");
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter result = new StringWriter();
		m.marshal(inspectionRequest, result);
		return result.toString();
	}

	private String getFindingsAsHtmlString(InspectionRequest inspectionRequest) {
		StringBuilder builder = new StringBuilder();
		Findings findings = inspectionRequest.getFindings();
		builder.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\">\n");
		builder.append("<html>\n");
		builder.append("<head>\n");
		builder.append("\n");
		builder.append("<title>\n");
		builder.append("Inspection report:"
				+ inspectionRequest.getSpreadsheetFile().getName() + "\n");
		builder.append("</title>\n");
		builder.append("</head>\n");
		builder.append("<body>\n");

		// CSS- Styles
		builder.append("<style type=\"text/css\">\n");
		builder.append("\n");
		builder.append("label {\n");
		builder.append("font-weight: bold;\n");
		builder.append("}\n");
		builder.append("\n");

		builder.append("td {\n");
		builder.append("align:center;\n");
		builder.append("}\n");
		builder.append("\n");

		builder.append("</style>\n");
		builder.append("\n");

		builder.append("<h1>\n");
		builder.append("Spreadsheet\n");
		builder.append("</h1>\n");
		builder.append("<label>"
				+ inspectionRequest.getInventory().getSpreadsheetFile()
						.getAbsolutePath() + "</label>\n");

		builder.append("<h1>\n");
		builder.append("Configuration\n");
		builder.append("</h1>\n");

		builder.append("<h2>\n");
		builder.append("Policy");
		builder.append("</h2>");

		builder.append("<label>\n");
		builder.append("Name: \n");
		builder.append("</label>\n");
		builder.append(inspectionRequest.getPolicy().getName() + "\n");
		builder.append("<br>\n");
		builder.append("\n");

		builder.append("<label>\n");
		builder.append("Author: \n");
		builder.append("</label>\n");
		builder.append(inspectionRequest.getPolicy().getAuthor() + "\n");
		builder.append("<br>\n");
		builder.append("\n");

		builder.append("<label>\n");
		builder.append("Description: \n");
		builder.append("</label>\n");
		builder.append(inspectionRequest.getPolicy().getDescription() + "\n");
		builder.append("\n");

		// Write Input-, Intermediate & Outpucells
		builder.append("<h1>\n");
		builder.append("Input cells\n");
		builder.append("</h1>\n");

		builder.append("<br>\n");

		builder.append("<table border=\"1px\" style=\"overflow: scroll\" >\n");

		builder.append("<thead style=\"table-layout:fixed;\">\n");
		builder.append(" <tr style=\"font-family:Tahoma; fontweight=bold; font-size:120%\">\n");
		builder.append("<th>Number</th>\n");
		builder.append("<th>Location</th>\n");
		builder.append("<th>Content</th>\n");
		builder.append(" </tr>\n");
		builder.append("</thead>\n");

		builder.append("<tbody>\n");

		AbstractElementList<InputCell> inputCells = inspectionRequest
				.getInventory().getListFor(InputCell.class);
		for (int i = 0; i < inputCells.getNumberOfElements(); i++) {
			InputCell currentCell = inputCells.getElements().get(i);
			builder.append("<td>" + i + "</td><td>" + currentCell.getLocation()
					+ "</td><td>" + currentCell.getStringRepresentation()
					+ "</td></tr>");
		}
		builder.append("</tbody>\n");
		builder.append("</table>\n");

		// Write Intermediate Cells
		builder.append("<h1>\n");
		builder.append("Intermediate cells\n");
		builder.append("</h1>\n");

		builder.append("<br>\n");

		builder.append("<table border=\"1px\" style=\"overflow: scroll\" >\n");

		builder.append("<thead style=\"table-layout:fixed;\">\n");
		builder.append(" <tr style=\"font-family:Tahoma; fontweight=bold; font-size:120%\">\n");
		builder.append("<th>Number</th>\n");
		builder.append("<th>Location</th>\n");
		builder.append("<th>Content</th>\n");
		builder.append(" </tr>\n");
		builder.append("</thead>\n");

		builder.append("<tbody>\n");

		AbstractElementList<IntermediateCell> intermediateCells = inspectionRequest
				.getInventory().getListFor(IntermediateCell.class);
		for (int i = 0; i < intermediateCells.getNumberOfElements(); i++) {
			IntermediateCell currentCell = intermediateCells.getElements().get(
					i);
			builder.append("<td>" + i + "</td><td>" + currentCell.getLocation()
					+ "</td><td>" + currentCell.getStringRepresentation()
					+ "</td></tr>");
		}

		builder.append("</tbody>\n");
		builder.append("</table>\n");

		// Write OutputCells
		builder.append("<h1>\n");
		builder.append("Output cells\n");
		builder.append("</h1>\n");

		builder.append("<br>\n");

		builder.append("<table border=\"1px\" style=\"overflow: scroll\" >\n");

		builder.append("<thead style=\"table-layout:fixed;\">\n");
		builder.append(" <tr style=\"font-family:Tahoma; fontweight=bold; font-size:120%\">\n");
		builder.append("<th>Number</th>\n");
		builder.append("<th>Location</th>\n");
		builder.append("<th>Content</th>\n");
		builder.append(" </tr>\n");
		builder.append("</thead>\n");

		builder.append("<tbody>\n");

		AbstractElementList<OutputCell> outputCells = inspectionRequest
				.getInventory().getListFor(OutputCell.class);
		for (int i = 0; i < outputCells.getNumberOfElements(); i++) {
			OutputCell currentCell = outputCells.getElements().get(i);
			builder.append("<td>" + i + "</td><td>" + currentCell.getLocation()
					+ "</td><td>" + currentCell.getStringRepresentation()
					+ "</td></tr>");
		}

		builder.append("</tbody>\n");
		builder.append("</table>\n");

		// Write Findings
		builder.append("<h1>\n");
		builder.append("Findings\n");
		builder.append("</h1>\n");

		for (ViolationList violationsList : findings.getViolationLists()) {

			builder.append("<h2>\n");
			builder.append(violationsList.getPolicyRule().getName() + "\n");
			builder.append(" (" + violationsList.getNumberOfViolations()
					+ " compounds; "
					+ violationsList.getNumberOfSingleViolations()
					+ " single violations)\n");
			builder.append("</h2>\n");
			builder.append("\n");
			builder.append("<label>Author: </label>"
					+ violationsList.getPolicyRule().getAuthor() + "<br>\n");
			builder.append("<label>Descritption: </label>"
					+ violationsList.getPolicyRule().getDescription()
					+ "<br>\n");
			builder.append("<label>Background: </label>"
					+ violationsList.getPolicyRule().getBackground() + "<br>\n");
			builder.append("<label>Chosen Severity Weight: </label>"
					+ violationsList.getPolicyRule().getSeverityWeight()
					+ "<br>\n");
			builder.append("<label>Possible Solution: </label>"
					+ violationsList.getPolicyRule().getPossibleSolution()
					+ "<br>\n");
			builder.append("<br>\n");
			builder.append("<br>\n");

			builder.append("<table border=\"1px\" style=\" width: 100%; overflow: scroll\" >\n");

			builder.append("<colgroup>\n");
			builder.append("<col width=\"50\">\n");
			builder.append("<col width=\"400\">\n");
			builder.append("<col width=\"200\">\n");
			builder.append("<col width=\"400\">\n");
			builder.append("<col width=\"50\">\n");
			builder.append("</colgroup>\n");

			builder.append("<thead style=\"table-layout:fixed;\">\n");
			builder.append(" <tr style=\"font-family:Tahoma; fontweight=bold; font-size:120%\">\n");
			builder.append("<th>Number</th>\n");
			builder.append("<th>Causing element</th>\n");
			builder.append("<th>Location</th>\n");
			builder.append("<th>Description</th>\n");
			builder.append("<th>Severity</th>\n");
			builder.append(" </tr>\n");
			builder.append("</thead>\n");

			builder.append("<tbody>\n");
			Integer counter = 0;
			for (IViolation violation : violationsList.getViolations()) {
				// Write a single vioaltion.
				if (violation instanceof ISingleViolation) {
					ISingleViolation singleViolation = (ISingleViolation) violation;
					counter++;
					builder.append(" <tr>\n");
					builder.append("<td>\n" + counter + "</td>\n");
					if (singleViolation.getCausingElement() != null) {
						builder.append("<td>\n"
								+ singleViolation.getCausingElement()
										.getStringRepresentation() + "</td>\n");
						builder.append("<td>\n"
								+ singleViolation.getCausingElement()
										.getLocation() + "</td>\n");
						builder.append("<td>"
								+ singleViolation.getDescription() + "</td>\n");

					} else {
						builder.append("<td>\n" + "unknown source" + "</td>\n");
						builder.append("<td>\n" + "-" + "</td>\n");
						builder.append("<td>"
								+ singleViolation.getDescription() + "</td>\n");
					}
					builder.append("<td>"
							+ singleViolation.getWeightedSeverityValue()
							+ "</td>\n");
					builder.append(" </tr>\n");
				} else {
					// Write a violation group;
					IViolationGroup violationGroup = (IViolationGroup) violation;
					builder.append("</tbody>\n");
					builder.append("<tbody bgcolor=\"lightgrey\">\n");
					builder.append("<tr align=\"left\"\n>");
					counter++;
					builder.append("<th>" + counter + "</th>\n");
					if (violationGroup.getCausingElement() != null) {
						builder.append("<th>\n"
								+ violationGroup.getCausingElement()
										.getStringRepresentation() + "</th>\n");
						builder.append("<th>\n"
								+ violationGroup.getCausingElement()
										.getLocation() + "</th>\n");
						builder.append("<th>" + violationGroup.getDescription()
								+ "</th>\n");
					} else {
						builder.append("<td>\n" + "unknown source" + "</td>\n");
						builder.append("<td>\n" + "-" + "</td>\n");
						builder.append("<td>" + "-" + "</td>\n");
					}
					builder.append("<th>"
							+ violationGroup.getWeightedSeverityValue()
							+ "</th>\n");
					builder.append(" </tr>\n");

					Integer groupCounter = 0;
					for (ISingleViolation singleViolation : violationGroup
							.getMembers()) {

						groupCounter++;
						builder.append(" <tr>\n");
						builder.append("<td>" + counter + "." + groupCounter
								+ "</td>\n");
						builder.append("<td>"
								+ singleViolation.getCausingElement()
										.getStringRepresentation() + "</td\n>");
						builder.append("<td>"
								+ singleViolation.getCausingElement()
										.getLocation() + "</td\n>");
						builder.append("<td>"
								+ singleViolation.getDescription() + "</td>\n");

						builder.append("<td>"
								+ singleViolation.getWeightedSeverityValue()
								+ "</td>\n");

						builder.append(" </tr>\n");

					}
					builder.append("</tbody>\n");
				}
			}

			builder.append("</tbody>\n");
			builder.append("</table>\n");
		}
		builder.append("</body>\n");
		builder.append("</html>\n");

		return builder.toString();
	}
}
