package no.fint.oneroster.model;

import lombok.Getter;
import lombok.Setter;
import no.fint.oneroster.model.vocab.RoleType;
import no.fint.oneroster.model.vocab.StatusType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class User extends Base {
    private final String username;
    private final Boolean enabledUser;
    private final String givenName;
    private final String familyName;
    private final RoleType role;
    private final List<GUIDRef> orgs;
    private List<UserId> userIds;
    private String middleName;
    private String identifier;
    private String email;
    private String sms;
    private String phone;
    private String password;
    private List<String> grades;
    private List<GUIDRef> agents;

    public User(String sourcedId, String username, Boolean enabledUser, String givenName, String familyName, RoleType role, List<GUIDRef> orgs) {
        super(sourcedId, StatusType.ACTIVE, ZonedDateTime.now(ZoneId.of("Z")));
        this.username = username;
        this.enabledUser = enabledUser;
        this.givenName = givenName;
        this.familyName = familyName;
        this.role = role;
        this.orgs = new ArrayList<>(orgs);
    }
}
