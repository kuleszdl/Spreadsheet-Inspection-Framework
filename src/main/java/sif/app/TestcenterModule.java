package sif.app;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import sif.testcenter.Facility;
import sif.testcenter.SpreadsheetInventory;
import sif.testcenter.custom_rules.CustomChecker;
import sif.testcenter.custom_rules.RulesFacility;
import sif.testcenter.dynamic_testing.ConditionChecker;
import sif.testcenter.dynamic_testing.DynamicTestingFacility;
import sif.testcenter.error_containing_cell.ErrorContainingCellFacility;
import sif.testcenter.formula_complexity.FormulaComplexityFacility;
import sif.testcenter.multiple_same_ref.MultipleSameRefFacility;
import sif.testcenter.no_constants.NoConstantsInFormulasFacility;
import sif.testcenter.non_considered_values.NonConsideredValuesFacility;
import sif.testcenter.one_among_others.OneAmongOthersFacility;
import sif.testcenter.reading_direction.ReadingDirectionFacility;
import sif.testcenter.ref_to_null.RefToNullFacility;
import sif.testcenter.sanity_checks.SanityChecksFacility;
import sif.testcenter.string_distance.StringDistanceFacility;

public class TestcenterModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<Facility> facilityBinder = Multibinder.newSetBinder(binder(), Facility.class);
        facilityBinder.addBinding().to(ErrorContainingCellFacility.class);
        facilityBinder.addBinding().to(FormulaComplexityFacility.class);
        facilityBinder.addBinding().to(MultipleSameRefFacility.class);
        facilityBinder.addBinding().to(NoConstantsInFormulasFacility.class);
        facilityBinder.addBinding().to(NonConsideredValuesFacility.class);
        facilityBinder.addBinding().to(OneAmongOthersFacility.class);
        facilityBinder.addBinding().to(ReadingDirectionFacility.class);
        facilityBinder.addBinding().to(RefToNullFacility.class);
        facilityBinder.addBinding().to(StringDistanceFacility.class);
        facilityBinder.addBinding().to(SanityChecksFacility.class);
        facilityBinder.addBinding().to(DynamicTestingFacility.class);
        facilityBinder.addBinding().to(RulesFacility.class);
        bind(SpreadsheetInventory.class);
        bind(ConditionChecker.class);
        bind(CustomChecker.class);
    }
}
