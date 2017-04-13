package sif.app;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import sif.api.InspectionService;
import sif.api.OdsResource;
import sif.api.OoxmlResource;
import sif.io.policy.PolicyIO;
import sif.io.policy.XmlPolicyIO;
import sif.io.spreadsheet.OdsSpreadsheetIO;
import sif.io.spreadsheet.OoxmlFormulaTransformer;
import sif.io.spreadsheet.OoxmlSpreadsheetIO;
import sif.io.spreadsheet.SpreadsheetIO;

@SuppressWarnings("PointlessBinding")
public class ApiModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SpreadsheetIO.class).annotatedWith(Names.named("ods")).to(OdsSpreadsheetIO.class);
        bind(SpreadsheetIO.class).annotatedWith(Names.named("ooxml")).to(OoxmlSpreadsheetIO.class);
        bind(PolicyIO.class).annotatedWith(Names.named("xml")).to(XmlPolicyIO.class);
        bind(OoxmlResource.class);
        bind(OoxmlFormulaTransformer.class);
        bind(OdsResource.class);
        bind(InspectionService.class);
    }
}
