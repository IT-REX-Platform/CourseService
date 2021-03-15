package de.uni_stuttgart.it_rex.course.domain.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.CONTENTREFERENCETYPE;
import de.uni_stuttgart.it_rex.course.utils.written.ContentReferenceUtil;
import de.uni_stuttgart.it_rex.course.utils.written.EnumUtil;
import de.uni_stuttgart.it_rex.course.utils.written.NumbersUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ContentReferenceTest {
    private static final UUID FIRST_ID = UUID.randomUUID();
    private static final UUID SECOND_ID = UUID.randomUUID();

    private static final int FIRST_INDEX =
        NumbersUtil.generateRandomInteger(0, 1000);
    private static final int SECOND_INDEX =
        NumbersUtil.generateRandomInteger(0, 1000);

    private static final UUID FIRST_CONTENT_ID = UUID.randomUUID();
    private static final UUID SECOND_CONTENT_ID = UUID.randomUUID();

    private static final CONTENTREFERENCETYPE FIRST_CONTENT_REFERENCE_TYPE =
        EnumUtil.generateRandomContentReferenceType();
    private static final CONTENTREFERENCETYPE SECOND_CONTENT_REFERENCE_TYPE =
        EnumUtil.generateRandomContentReferenceType();

    @Test
    void equalsVerifier() throws Exception {
        ContentReference cr1 = new ContentReference();
        cr1.setId(FIRST_ID);
        cr1.setIndex(FIRST_INDEX);
        cr1.setContentId(FIRST_CONTENT_ID);
        cr1.setContentReferenceType(FIRST_CONTENT_REFERENCE_TYPE);

        ContentReference cr2  = new ContentReference();
        cr2.setId(SECOND_ID);
        cr2.setIndex(SECOND_INDEX);
        cr2.setContentId(SECOND_CONTENT_ID);
        cr2.setContentReferenceType(SECOND_CONTENT_REFERENCE_TYPE);

        ContentReference cr3 = new ContentReference();
        cr3.setId(FIRST_ID);
        cr3.setIndex(FIRST_INDEX);
        cr3.setContentId(FIRST_CONTENT_ID);
        cr3.setContentReferenceType(FIRST_CONTENT_REFERENCE_TYPE);

        assertEquals(cr1.hashCode(), cr1.hashCode());
        assertEquals(cr1, cr1);

        ContentReferenceUtil.equalsContentReference(cr1, cr3);

        assertNotEquals(cr1, cr2);
    }
}
