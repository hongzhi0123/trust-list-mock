<?xml version="1.0" encoding="UTF-8" standalone="no"?><TrustServiceStatusList xmlns="http://uri.etsi.org/02231/v2#" xmlns:ns2="http://www.w3.org/2000/09/xmldsig#" xmlns:ns3="http://uri.etsi.org/02231/v2/additionaltypes#" xmlns:ns4="http://uri.etsi.org/01903/v1.3.2#" xmlns:ns5="http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#" xmlns:ns6="http://uri.etsi.org/01903/v1.4.1#" Id="LOTL1235" TSLTag="http://uri.etsi.org/19612/TSLTag">
    <SchemeInformation>
        <TSLVersionIdentifier>6</TSLVersionIdentifier>
        <TSLSequenceNumber>1</TSLSequenceNumber>
        <TSLType>http://uri.etsi.org/TrstSvc/TrustedList/TSLType/EUlistofthelists</TSLType>
        <SchemeOperatorName>
            <Name xml:lang="en">EU Operator name</Name>
        </SchemeOperatorName>
        <SchemeOperatorAddress>
            <PostalAddresses>
                <PostalAddress xml:lang="en">
                    <StreetAddress>TEST</StreetAddress>
                    <Locality>TEST</Locality>
                    <PostalCode>TEST</PostalCode>
                    <CountryName>EU</CountryName>
                </PostalAddress>
            </PostalAddresses>
            <ElectronicAddress>
                <URI xml:lang="en">mailto:test@test.test</URI>
            </ElectronicAddress>
        </SchemeOperatorAddress>
        <SchemeName>
            <Name xml:lang="en">EU: TEST</Name>
        </SchemeName>
        <SchemeInformationURI>
            <URI xml:lang="en">https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=uriserv:OJ.C_.2019.276.01.0001.01.ENG</URI>
            <URI xml:lang="en">https://ec.europa.eu/tools/lotl/eu-lotl-legalnotice.html#en</URI>
        </SchemeInformationURI>
        <StatusDeterminationApproach>http://uri.etsi.org/TrstSvc/TrustedList/StatusDetn/EUlistofthelists</StatusDeterminationApproach>
        <SchemeTypeCommunityRules>
            <URI xml:lang="en">http://uri.etsi.org/TrstSvc/TrustedList/schemerules/EUlistofthelists</URI>
        </SchemeTypeCommunityRules>
        <SchemeTerritory>EU</SchemeTerritory>
        <PolicyOrLegalNotice>
            <TSLLegalNotice xml:lang="en">TEST</TSLLegalNotice>
        </PolicyOrLegalNotice>
        <HistoricalInformationPeriod>65535</HistoricalInformationPeriod>
        <PointersToOtherTSL>
            <#list tslList as tsl>
            <OtherTSLPointer>
                <ServiceDigitalIdentities>
                    <ServiceDigitalIdentity>
                        <DigitalId>
                            <X509Certificate>${tsl.certificate}</X509Certificate>
                        </DigitalId>
                    </ServiceDigitalIdentity>
                </ServiceDigitalIdentities>
                <TSLLocation>${tsl.location}</TSLLocation>
                <AdditionalInformation>
                    <OtherInformation>
                        <SchemeTerritory>${tsl.countryCode}</SchemeTerritory>
                    </OtherInformation>
                    <OtherInformation>
                        <TSLType>http://uri.etsi.org/TrstSvc/TrustedList/TSLType/EUlistofthelists</TSLType>
                    </OtherInformation>
                    <OtherInformation>
                        <SchemeOperatorName>
                            <Name xml:lang="en">${tsl.countryCode} Operator name</Name>
                        </SchemeOperatorName>
                    </OtherInformation>
                    <OtherInformation>
                        <ns3:MimeType>application/vnd.etsi.tsl+xml</ns3:MimeType>
                    </OtherInformation>
                </AdditionalInformation>
            </OtherTSLPointer>
            </#list>
        </PointersToOtherTSL>
        <ListIssueDateTime>${issueDateTime}</ListIssueDateTime>
        <NextUpdate>
            <dateTime>${updateDateTime}</dateTime>
        </NextUpdate>
    </SchemeInformation>
</TrustServiceStatusList>