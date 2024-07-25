package nl.rgs.kib.models;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.rgs.kib.model.file.KibFile;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KibFileTest {

    @Nested
    public class KibFileTestValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        @Test
        public void testKibFileIdNotNullValidator() {
            KibFile kibFile = new KibFile();
            kibFile.setId(null);
            kibFile.setName("photo.jpg");
            kibFile.setType("image/jpeg");
            kibFile.setData(new byte[0]);

            assertEquals(1, validator.validate(kibFile).size(), "Id should not be null");
        }

        @Test
        public void testKibFileNameNotNullValidator() {
            KibFile kibFile = new KibFile();
            kibFile.setId(new ObjectId().toHexString());
            kibFile.setName(null);
            kibFile.setType("image/jpeg");
            kibFile.setData(new byte[0]);

            assertEquals(1, validator.validate(kibFile).size(), "Name should not be null");
        }

        @Test
        public void testKibFileNameNotBlankValidator() {
            KibFile kibFile = new KibFile();
            kibFile.setId(new ObjectId().toHexString());
            kibFile.setName("");
            kibFile.setType("image/jpeg");
            kibFile.setData(new byte[0]);

            assertEquals(1, validator.validate(kibFile).size(), "Name should not be blank");
        }

        @Test
        public void testKibFileTypeNotNullValidator() {
            KibFile kibFile = new KibFile();
            kibFile.setId(new ObjectId().toHexString());
            kibFile.setName("photo.jpg");
            kibFile.setType(null);
            kibFile.setData(new byte[0]);

            assertEquals(1, validator.validate(kibFile).size(), "Type should not be null");
        }

        @Test
        public void testKibFileTypeNotBlankValidator() {
            KibFile kibFile = new KibFile();
            kibFile.setId(new ObjectId().toHexString());
            kibFile.setName("photo.jpg");
            kibFile.setType("");
            kibFile.setData(new byte[0]);

            assertEquals(1, validator.validate(kibFile).size(), "Type should not be blank");
        }

        @Test
        public void testKibFileDataNotNullValidator() {
            KibFile kibFile = new KibFile();
            kibFile.setId(new ObjectId().toHexString());
            kibFile.setName("photo.jpg");
            kibFile.setType("image/jpeg");
            kibFile.setData(null);

            assertEquals(1, validator.validate(kibFile).size(), "Data should not be null");
        }
    }
}
