package sif.api;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.Facility;
import sif.testcenter.InspectionResponse;
import sif.testcenter.SpreadsheetInventory;
import sif.scanner.Scanner;

import java.util.HashSet;
import java.util.Set;

@RequestScoped
public class InspectionService {

    private final Logger logger = LoggerFactory.getLogger(InspectionService.class);

    private final SpreadsheetInventory spreadsheetInventory;
    private Set<Facility> facilities = new HashSet<>();
    private Set<Scanner> scanners = new HashSet<>();

    @Inject
    private InspectionService(
            SpreadsheetInventory spreadsheetInventory,
            Set<Facility> facilities,
            Set<Scanner> scanners

    ) {
        this.spreadsheetInventory = spreadsheetInventory;
        this.facilities = facilities;
        this.scanners = scanners;
    }

    InspectionResponse generateResponse() {
        scan();
        run();
        return spreadsheetInventory.getInspectionResponse();
    }

    private void run() {
        for (Facility facility : facilities) {
            if (facility.runCheck()) {
                logger.trace("Executing '{}.run()'", facility.getClass().getSimpleName());
                facility.run();
            }
        }
    }

    private void scan() {
        for (Scanner scanner : scanners) {
            logger.trace("Executing '{}'.scan()", scanner.getClass().getSimpleName());
            scanner.scan();
        }
    }

    public SpreadsheetInventory getSpreadsheetInventory() { return spreadsheetInventory; }
}
