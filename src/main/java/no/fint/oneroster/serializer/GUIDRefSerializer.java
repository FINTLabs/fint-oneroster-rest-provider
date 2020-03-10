package no.fint.oneroster.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import no.fint.oneroster.model.*;
import no.fint.oneroster.model.vocab.GUIDType;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final Map<GUIDType, String> paths = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>(GUIDType.ACADEMICSESSION, "academicSessions"),
            new AbstractMap.SimpleImmutableEntry<>(GUIDType.CLASS, "classes"),
            new AbstractMap.SimpleImmutableEntry<>(GUIDType.COURSE, "courses"),
            new AbstractMap.SimpleImmutableEntry<>(GUIDType.ENROLLMENT, "enrollments"),
            new AbstractMap.SimpleImmutableEntry<>(GUIDType.ORG, "orgs"),
            new AbstractMap.SimpleImmutableEntry<>(GUIDType.USER, "users"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
}