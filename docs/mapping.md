# IMS OneRoster v1.1

## Mapping table: FINT to IMS OneRoster

### Org

> district

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | configuration                                                | 1
status           | auto                                                         | 1
dateLastModified | auto                                                         | 1
name             | configuration                                                | 1
type             | district                                                     | 1
identifier       | configuration                                                | 0..1
parent           | n/a                                                          | 0..1
children         | skole.systemId.identifikatorverdi                            | 0..*
metadata         | -                                                            | 0..1

> school

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | skole.systemId.identifikatorverdi                        | 1
status           | auto                                                     | 1
dateLastModified | auto                                                     | 1
name             | skole.navn                                               | 1
type             | school                                                   | 1
identifier       | skole.organisasjonsnummer.identifikatorverdi             | 0..1
parent           | org.sourcedId                                            | 0..1
children         | n/a                                                      | 0..*
metadata         | -                                                        | 0..1

### User

> student

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | elev.systemId.identifikatorverdi             | 1
status           | auto                                         | 1
dateLastModified | auto                                         | 1
username         | elev.brukernavn.identifikatorverdi           | 1
givenName        | person.navn.fornavn                          | 1
familyName       | person.navn.etternavn                        | 1
role             | student                                      | 1
orgs             | skole.systemId.identifikatorverdi            | 1..*
userIds          | elev.feidenavn.identifikatorverdi            | 0..*
middleName       | person.navn.mellomnavn                       | 0..1
identifier       | person.fodselsnummer.identifikatorverdi      | 0..1
email            | elev.kontaktinformasjon.epostadresse         | 0..1
sms              | -                                            | 0..1
phone            | -                                            | 0..1
password         | -                                            | 0..1
grades           | -                                            | 0..*
agents           | -                                            | 0..*
metadata         | -                                            | 0..1

> teacher

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | skoleressurs.systemId.identifikatorverdi             | 1
status           | auto                                                 | 1
dateLastModified | auto                                                 | 1
username         | personalressurs.brukernavn.identifikatorverdi        | 1
givenName        | person.navn.fornavn                                  | 1
familyName       | person.navn.etternavn                                | 1
role             | teacher                                              | 1
orgs             | skole.systemId.identifikatorverdi                    | 1..*
userIds          | skoleressurs.feidenavn.identifikatorverdi            | 0..*
middleName       | person.navn.mellomnavn                               | 0..1
identifier       | person.fodselsnummer.identifikatorverdi              | 0..1
email            | personalressurs.kontaktinformasjon.epostadresse      | 0..1
sms              | -                                                    | 0..1
phone            | -                                                    | 0..1
password         | -                                                    | 0..1
grades           | -                                                    | 0..*
agents           | -                                                    | 0..*
metadata         | -                                                    | 0..1

### Enrollment

> student

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | elevforhold.systemId.identifikatorverdi + gruppe.systemId.identifikatorverdi | 1
status           | auto                                                     | 1
dateLastModified | auto                                                     | 1
user             | elev.systemId.identifikatorverdi                         | 1
class            | (basis/undervisnings/kontaktlærer)gruppe.systemId.identifikatorverdi | 1
school           | skole.systemId.identifikatorverdi                        | 1
role             | student                                                  | 1
primary          | -                                                        | 0..1
beginDate        | -                                                        | 0..1
endDate          | -                                                        | 0..1
metadata         | -                                                        | 0..1

> teacher

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | undervisningsforhold.systemId.identifikatorverdi + gruppe.systemId.identifikatorverdi | 1
status           | auto                                                     | 1
dateLastModified | auto                                                     | 1
user             | skoleressurs.systemId.identifikatorverdi                 | 1
class            | (basis/undervisnings/kontaktlærer)gruppe.systemId.identifikatorverdi  | 1
school           | skole.systemId.identifikatorverdi                        | 1
role             | teacher                                                  | 1
primary          | -                                                        | 0..1
beginDate        | -                                                        | 0..1
endDate          | -                                                        | 0..1
metadata         | -                                                        | 0..1

### Class

> homeroom

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | (basis/kontaktlærer)gruppe.systemId.identifikatorverdi  | 1
status           | auto                                     | 1
dateLastModified | auto                                     | 1
title            | (basis/kontaktlærer)gruppe.navn                         | 1
classType        | homeroom                                 | 1
course           | arstrinn.systemId.identifikatorverdi     | 1
school           | skole.systemId.identifikatorverdi        | 1
terms            | termin.systemId.identifikatorverdi       | 1..*
classCode        | -                                        | 0..1
location         | -                                        | 0..1
grades           | -                                        | 0..*
subjects         | -                                        | 0..*
subjectCodes     | -                                        | 0..*
periods          | -                                        | 0..*
metadata         | additionalClassType                      | 0..1

> scheduled

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | undervisningsgruppe.systemId.identifikatorverdi  | 1
status           | auto                                             | 1
dateLastModified | auto                                             | 1
title            | undervisningsgruppe.navn                         | 1
classType        | scheduled                                        | 1
course           | fag.systemId.identifikatorverdi                  | 1
school           | skole.systemId.identifikatorverdi                | 1
terms            | termin.systemId.identifikatorverdi               | 1..*
classCode        | -                                                | 0..1
location         | -                                                | 0..1
grades           | -                                                | 0..*
subjects         | -                                                | 0..*
subjectCodes     | (fag/arstrinn).grepreferanse                     | 0..*
periods          | -                                                | 0..*
metadata         | -                                                | 0..1

### Course

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | (fag/arstrinn).systemId.identifikatorverdi               | 1
status           | auto                                                     | 1
dateLastModified | auto                                                     | 1
title            | (fag/arstrinn).navn                                      | 1
org              | org.sourcedId                                            | 1
schoolYear       | -                                                        | 0..1
courseCode       | -                                                        | 0..1
grades           | -                                                        | 0..*
subjects         | -                                                        | 0..*
subjectCodes     | (fag/arstrinn).grepreferanse                             | 0..*
metadata         | -                                                        | 0..1

### AcademicSession

> term

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | termin.systemId.identifikatorverdi   | 1
status           | auto                                 | 1
dateLastModified | auto                                 | 1
title            | termin.navn                          | 1
startDate        | termin.gyldighetsperiode.start       | 1
endDate          | termin.gyldighetsperiode.slutt       | 1
type             | term                                 | 1
schoolYear       | skolear.gyldighetsperiode.slutt      | 1
parent           | n/a                                  | 0..1
children         | n/a                                  | 0..*
metadata         | -                                    | 0..1