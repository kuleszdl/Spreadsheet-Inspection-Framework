package sif.app;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import sif.model.AddressFactory;
import sif.model.ElementFactory;
import sif.model.Spreadsheet;
import sif.model.tokens.TokenFactory;
import sif.model.values.ValueFactory;
import sif.model.values.ValueHelper;

@SuppressWarnings("PointlessBinding")
public class ModelModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Spreadsheet.class);
        bind(ValueHelper.class);
        install(new FactoryModuleBuilder().build(AddressFactory.class));
        install(new FactoryModuleBuilder().build(TokenFactory.class));
        install(new FactoryModuleBuilder().build(ElementFactory.class));
        install(new FactoryModuleBuilder().build(ValueFactory.class));
    }
}
