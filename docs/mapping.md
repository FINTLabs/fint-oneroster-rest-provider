# IMS OneRoster v1.1

## Rostering core

### Org

> district (*)

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | Organisasjonselement.organisasjonsId.identifikatorverdi      | 1
status           | active                                                       | 1
dateLastModified | ZonedDateTime.now()                                          | 1
name             | Organisasjonselement.navn                                    | 1
type             | district                                                     | 1
identifier       | Organisasjonselement.organisasjonsnummer.identifikatorverdi  | 0..1
parent           | n/a                                                          | 0..1
children         | Skole.systemId.identifikatorverdi                            | 0..*
metadata         | -                                                            | 0..1

> school

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | Skole.systemId.identifikatorverdi                        | 1
status           | active                                                   | 1
dateLastModified | ZonedDateTime.now()                                      | 1
name             | Skole.navn                                               | 1
type             | school                                                   | 1
identifier       | Skole.organisasjonsnummer.identifikatorverdi             | 0..1
parent           | Organisasjonselement.organisasjonsId.identifikatorverdi  | 0..1
children         | n/a                                                      | 0..*
metadata         | -                                                        | 0..1

### User

> student

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | Elev.systemId.identifikatorverdi             | 1
status           | active                                       | 1
dateLastModified | ZonedDateTime.now()                          | 1
username         | Elev.brukernavn.identifikatorverdi           | 1
givenName        | Person.navn.fornavn                          | 1
familyName       | Person.navn.etternavn                        | 1
role             | student                                      | 1
orgs             | Skole.systemId.identifikatorverdi            | 1..*
userIds          | Elev.feidenavn.identifikatorverdi            | 0..*
middleName       | Person.navn.mellomnavn                       | 0..1
identifier       | Elev.elevnummer.identifikatorverdi           | 0..1
email            | Elev.kontaktinformasjon.epostadresse         | 0..1
sms              | Elev.kontaktinformasjon.mobiltelefonnummer   | 0..1
phone            | Elev.kontaktinformasjon.telefonnummer        | 0..1
password         | -                                            | 0..1
grades           | -                                            | 0..*
agents           | -                                            | 0..*
metadata         | -                                            | 0..1

> teacher

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | Skoleressurs.systemId.identifikatorverdi             | 1
status           | active                                               | 1
dateLastModified | ZonedDateTime.now()                                  | 1
username         | Personalressurs.brukernavn.identifikatorverdi        | 1
givenName        | Person.navn.fornavn                                  | 1
familyName       | Person.navn.etternavn                                | 1
role             | teacher                                              | 1
orgs             | Skole.systemId.identifikatorverdi                    | 1..*
userIds          | Skoleressurs.feidenavn.identifikatorverdi            | 0..*
middleName       | Person.navn.mellomnavn                               | 0..1
identifier       | Personalressurs.ansattnummer.identifikatorverdi      | 0..1
email            | Personalressurs.kontaktinformasjon.epostadresse      | 0..1
sms              | Personalressurs.kontaktinformasjon.mobiltelefonnummer| 0..1
phone            | Personalressurs.kontaktinformasjon.telefonnummer     | 0..1
password         | -                                                    | 0..1
grades           | -                                                    | 0..*
agents           | -                                                    | 0..*
metadata         | -                                                    | 0..1

### Enrollment

> student

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | ?                                                        | 1
status           | active                                                   | 1
dateLastModified | ZonedDateTime.now()                                      | 1
user             | Elev.systemId.identifikatorverdi                         | 1
class            | (Basis/Undervisnings)gruppe.systemId.identifikatorverdi  | 1
school           | Skole.systemId.identifikatorverdi                        | 1
role             | student                                                  | 1
primary          | n/a                                                      | 0..1
beginDate        | -                                                        | 0..1
endDate          | -                                                        | 0..1
metadata         | -                                                        | 0..1

> teacher

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | ?                                                        | 1
status           | active                                                   | 1
dateLastModified | ZonedDateTime.now()                                      | 1
user             | Skoleressurs.systemId.identifikatorverdi                 | 1
class            | (Basis/Undervisnings)gruppe.systemId.identifikatorverdi  | 1
school           | Skole.systemId.identifikatorverdi                        | 1
role             | teacher                                                  | 1
primary          | -                                                        | 0..1
beginDate        | -                                                        | 0..1
endDate          | -                                                        | 0..1
metadata         | -                                                        | 0..1

### Class

> homeroom

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | Basisgruppe.systemId.identifikatorverdi  | 1
status           | active                                   | 1
dateLastModified | ZonedDateTime.now()                      | 1
title            | Basisgruppe.navn                         | 1
classType        | homeroom                                 | 1
course           | Arstrinn.systemId.identifikatorverdi     | 1
school           | Skole.systemId.identifikatorverdi        | 1
terms            | AcademicSession.sourcedId (*)            | 1..*
classCode        | -                                        | 0..1
location         | -                                        | 0..1
grades           | -                                        | 0..*
subjects         | -                                        | 0..*
subjectCodes     | -                                        | 0..*
periods          | -                                        | 0..*
metadata         | -                                        | 0..1

> scheduled

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | Undervisningsgruppe.systemId.identifikatorverdi  | 1
status           | active                                           | 1
dateLastModified | ZonedDateTime.now()                              | 1
title            | Undervisningsgruppe.navn                         | 1
classType        | scheduled                                        | 1
course           | Fag.systemId.identifikatorverdi                  | 1
school           | Skole.systemId.identifikatorverdi                | 1
terms            | AcademicSession.sourcedId (*)                    | 1..*
classCode        | -                                                | 0..1
location         | -                                                | 0..1
grades           | -                                                | 0..*
subjects         | -                                                | 0..*
subjectCodes     | -                                                | 0..*
periods          | -                                                | 0..*
metadata         | -                                                | 0..1

### Course

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | Fag/Arstrinn.systemId.identifikatorverdi                 | 1
status           | active                                                   | 1
dateLastModified | ZonedDateTime.now()                                      | 1
title            | Fag/Arstrinn.navn                                        | 1
org              | Organisasjonselement.organisasjonsId.identifikatorverdi  | 1
schoolYear       | - (*)                                                    | 0..1
courseCode       | Fag/Arstrinn.grepreferanse                               | 0..1
grades           | -                                                        | 0..*
subjects         | -                                                        | 0..*
subjectCodes     | -                                                        | 0..*
metadata         | -                                                        | 0..1

### AcademicSession

> term

OneRoster | FINT | Multiplicity
--------- | ---- | ------------
sourcedId        | Generated OR Env                     | 1
status           | active                               | 1
dateLastModified | ZonedDateTime.now()                  | 1
title            | Generated OR Env                     | 1
startDate        | Generated OR Env                     | 1
endDate          | Generated OR Env                     | 1
type             | term                                 | 1
schoolYear       | Generated OR Env                     | 1
parent           | -                                    | 0..1
children         | n/a                                  | 0..*
metadata         | -                                    | 0..1

> schoolYear?
