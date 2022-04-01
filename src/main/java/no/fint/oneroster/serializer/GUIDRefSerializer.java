package no.fint.oneroster.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.vocab.GUIDType;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@JsonComponent
public class GUIDRefSerializer extends JsonSerializer<GUIDRef> {

    @Override
    public void serialize(GUIDRef guidRef, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("sourcedId", guidRef.getSourcedId());
        gen.writeObjectField("type", guidRef.getType());
        gen.writeStringField("href", getHref(guidRef));
        gen.writeEndObject();
    }

    private String getHref(GUIDRef guidRef) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .pathSegment(paths.get(guidRef.getType()), guidRef.getSourcedId())
                .build()
                .toString();
    }

    private final Map<GUIDType, String> paths = Map.of(
            GUIDType.ACADEMICSESSION, "academicSessions",
            GUIDType.CLASS, "classes",
            GUIDType.COURSE, "courses",
            GUIDType.ENROLLMENT, "enrollments",
            GUIDType.ORG, "orgs",
            GUIDType.USER, "users");
}