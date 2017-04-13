package sif.io.policy;

import sif.testcenter.InspectionRequest;

import java.io.InputStream;

public interface PolicyIO {
    InspectionRequest createInspectionRequest(InputStream inputStream) throws InvalidPolicyException;
}
