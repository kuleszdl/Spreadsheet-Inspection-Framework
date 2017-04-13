package siftest.xml;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import siftest.GuiceJUnitRunner;
import siftest.TestModule;
import sif.model.values.ValueType;
import sif.testcenter.InspectionRequest;
import sif.testcenter.InspectionResponse;
import sif.testcenter.Policy;
import sif.testcenter.dynamic_testing.*;
import sif.testcenter.error_containing_cell.ErrorContainingCellPolicy;
import sif.testcenter.formula_complexity.FormulaComplexityPolicy;
import sif.testcenter.multiple_same_ref.MultipleSameRefPolicy;
import sif.testcenter.no_constants.NoConstantsInFormulasPolicy;
import sif.testcenter.non_considered_values.NonConsideredValuesPolicy;
import sif.testcenter.one_among_others.OneAmongOthersPolicy;
import sif.testcenter.reading_direction.ReadingDirectionPolicy;
import sif.testcenter.ref_to_null.RefToNullPolicy;
import sif.testcenter.string_distance.StringDistancePolicy;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({ TestModule.class })

public class XmlTest {

    private final Logger logger = LoggerFactory.getLogger(XmlTest.class);
    private static final String BASE_PATH = "src/test/resources/";

    private DynamicTestingPolicy createDynamicPolicy() {

        DynamicTestingPolicy dynamicTestingPolicy = new DynamicTestingPolicy();

        Scenario scenario = new Scenario("Testszenario");
        dynamicTestingPolicy.addScenario(scenario);

        List<Input> inputs = new ArrayList<>();
        inputs.add(new Input("Tabelle1!A1", "10", ValueType.NUMERIC));
        inputs.add(new Input("Tabelle1!B2", "5", ValueType.NUMERIC));
        scenario.setInputs(inputs);

        List<String> invariants = new ArrayList<>();
        invariants.add("A2");
        scenario.setInvariants(invariants);

        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Condition("Tabelle1!C1", Operator.valueOf("EQUALS"), "20", ValueType.NUMERIC));
        conditions.add(new Condition("Tabelle1!B2", Operator.valueOf("GREATER"), "20", ValueType.NUMERIC));
        scenario.setConditions(conditions);
        return dynamicTestingPolicy;
    }

    @Test
    public void policyOutputTest() {

        InspectionRequest inspectionRequest = new InspectionRequest();

        List<String> ignoredCells = new ArrayList<>();
        ignoredCells.add("Tabelle1!A1");
        ignoredCells.add("Tabelle1!A2");
        inspectionRequest.setIgnoredCells(ignoredCells);

        List<String> ignoredWorksheets = new ArrayList<>();
        ignoredWorksheets.add("Tabelle2");
        ignoredWorksheets.add("Tabelle3");
        inspectionRequest.setIgnoredWorksheets(ignoredWorksheets);

        List<Policy> list = new ArrayList<>();
        list.add(new ErrorContainingCellPolicy());
        list.add(new FormulaComplexityPolicy(2, 3));
        list.add(new MultipleSameRefPolicy());
        list.add(new NoConstantsInFormulasPolicy());
        list.add(new NonConsideredValuesPolicy());
        list.add(new OneAmongOthersPolicy("vertical", 2));
        list.add(new ReadingDirectionPolicy(true, true));
        list.add(new RefToNullPolicy());
        list.add(new StringDistancePolicy(3));
        list.add(createDynamicPolicy());

        inspectionRequest.setPolicies(list);

        JAXBContext jc = null;
        try {
            File file = new File(BASE_PATH + "/policies/policy-output-test.xml");
            jc = JAXBContext.newInstance(InspectionRequest.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(inspectionRequest, file);
        } catch (JAXBException e) {
            logger.error("Exception thrown: ", e);
        }
        Assert.assertNotNull(jc);
    }

    public void inspectionRequestXsdGeneration() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(InspectionRequest.class);
            XsdGenerator xsdGenerator = new XsdGenerator();
            xsdGenerator.createOutput("", BASE_PATH + "/xsd/inspectionReport.xsd");
            jaxbContext.generateSchema(xsdGenerator);
        } catch (IOException | JAXBException e) {
            logger.error("Exception thrown: ", e);
        }
    }

    public void inspectionReportXsdGeneration() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(InspectionResponse.class);
            XsdGenerator xsdGenerator = new XsdGenerator();
            jaxbContext.generateSchema(xsdGenerator);
            xsdGenerator.createOutput("", BASE_PATH + "/xsd/inspectionRequest.xsd");
        } catch (IOException | JAXBException e) {
            logger.error("Exception thrown: ", e);
        }
    }
}
