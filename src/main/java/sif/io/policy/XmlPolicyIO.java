package sif.io.policy;

import com.google.inject.servlet.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sif.testcenter.InspectionRequest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.InputStream;

@RequestScoped
public class XmlPolicyIO implements PolicyIO {

    private final Logger logger = LoggerFactory.getLogger(XmlPolicyIO.class);

    XmlPolicyIO() {}

    @Override
    public InspectionRequest createInspectionRequest(InputStream inputStream) throws InvalidPolicyException {
        InspectionRequest inspectionRequest;
        try {
            JAXBContext jc = JAXBContext.newInstance(InspectionRequest.class);
            inspectionRequest = (InspectionRequest) jc.createUnmarshaller().unmarshal(inputStream);
        } catch (JAXBException e) {
            logger.error(e.getClass().getSimpleName());
            logger.debug(e.getClass().getSimpleName(), e);
            throw new InvalidPolicyException();
        }
        return inspectionRequest;
    }
}
