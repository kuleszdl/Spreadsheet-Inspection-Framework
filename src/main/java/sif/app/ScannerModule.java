package sif.app;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import sif.scanner.InputCellScanner;
import sif.scanner.IntermediateCellScanner;
import sif.scanner.OutputCellScanner;
import sif.scanner.Scanner;

public class ScannerModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<Scanner> scannerMultibinder = Multibinder.newSetBinder(binder(), Scanner.class);
        scannerMultibinder.addBinding().to(InputCellScanner.class);
        scannerMultibinder.addBinding().to(IntermediateCellScanner.class);
        scannerMultibinder.addBinding().to(OutputCellScanner.class);
    }
}
