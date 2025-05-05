<?xml version="1.0" encoding="UTF-8" standalone="no"?><TrustServiceStatusList xmlns="http://uri.etsi.org/02231/v2#" xmlns:ns2="http://www.w3.org/2000/09/xmldsig#" xmlns:ns3="http://uri.etsi.org/02231/v2/additionaltypes#" xmlns:ns4="http://uri.etsi.org/01903/v1.3.2#" xmlns:ns5="http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#" xmlns:ns6="http://uri.etsi.org/01903/v1.4.1#" Id="TL12345" TSLTag="http://uri.etsi.org/19612/TSLTag">
    <SchemeInformation>
        <TSLVersionIdentifier>6</TSLVersionIdentifier>
        <TSLSequenceNumber>1</TSLSequenceNumber>
        <TSLType>http://uri.etsi.org/TrstSvc/TrustedList/TSLType/EUgeneric</TSLType>
        <SchemeOperatorName>
            <Name xml:lang="en">${countryCode} Operator name</Name>
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
                <URI xml:lang="en">+352123456789</URI>
            </ElectronicAddress>
        </SchemeOperatorAddress>
        <SchemeName>
            <Name xml:lang="en">${countryCode}: TEST</Name>
        </SchemeName>
        <SchemeInformationURI>
            <URI xml:lang="en">https://eidas.ec.europa.eu/efda/api/v2/validation-tests/testcase/test-disclaimer/</URI>
        </SchemeInformationURI>
        <StatusDeterminationApproach>https://eidas.ec.europa.eu/efda/api/v2/validation-tests/testcase/test-disclaimer/</StatusDeterminationApproach>
        <SchemeTypeCommunityRules>
            <URI xml:lang="en">http://uri.etsi.org/TrstSvc/TrustedList/schemerules/EUcommon</URI>
            <URI xml:lang="en">http://uri.etsi.org/TrstSvc/TrustedList/schemerules/${countryCode}</URI>
        </SchemeTypeCommunityRules>
        <SchemeTerritory>${countryCode}</SchemeTerritory>
        <PolicyOrLegalNotice>
            <TSLLegalNotice xml:lang="en">TEST</TSLLegalNotice>
        </PolicyOrLegalNotice>
        <HistoricalInformationPeriod>65535</HistoricalInformationPeriod>
        <PointersToOtherTSL>
            <OtherTSLPointer>
                <ServiceDigitalIdentities>
                    <ServiceDigitalIdentity>
                        <DigitalId>
                            <X509Certificate>${lotl.certificate}</X509Certificate>
                        </DigitalId>
                    </ServiceDigitalIdentity>
                </ServiceDigitalIdentities>
                <TSLLocation>${lotl.location}</TSLLocation>
                <AdditionalInformation>
                    <OtherInformation>
                        <SchemeTerritory>EU</SchemeTerritory>
                    </OtherInformation>
                    <OtherInformation>
                        <TSLType>http://uri.etsi.org/TrstSvc/TrustedList/TSLType/EUlistofthelists</TSLType>
                    </OtherInformation>
                    <OtherInformation>
                        <SchemeOperatorName>
                            <Name xml:lang="en">EU Operator name</Name>
                        </SchemeOperatorName>
                    </OtherInformation>
                    <OtherInformation>
                        <ns3:MimeType>application/vnd.etsi.tsl+xml</ns3:MimeType>
                    </OtherInformation>
                </AdditionalInformation>
            </OtherTSLPointer>
        </PointersToOtherTSL>
        <ListIssueDateTime>${lotl.issueDateTime}</ListIssueDateTime>
        <NextUpdate>
            <dateTime>${lotl.updateDateTime}</dateTime>
        </NextUpdate>
    </SchemeInformation>
    <TrustServiceProviderList>
        <#list tspList as tsp>
        <TrustServiceProvider>
            <TSPInformation>
                <TSPName>
                    <Name xml:lang="en">TSP-${tsp.id} Name</Name>
                </TSPName>
                <TSPTradeName>
                    <Name xml:lang="en">TSP-${tsp.id} Trade name</Name>
                </TSPTradeName>
                <TSPAddress>
                    <PostalAddresses>
                        <PostalAddress xml:lang="en">
                            <StreetAddress>Street address</StreetAddress>
                            <Locality>Locality</Locality>
                            <StateOrProvince>Province</StateOrProvince>
                            <PostalCode>123</PostalCode>
                            <CountryName>${countryCode}</CountryName>
                        </PostalAddress>
                    </PostalAddresses>
                    <ElectronicAddress>
                        <URI xml:lang="en">mailto:test@test.test</URI>
                    </ElectronicAddress>
                </TSPAddress>
                <TSPInformationURI>
                    <URI xml:lang="en">http://tsp.information.uri/test</URI>
                </TSPInformationURI>
            </TSPInformation>
            <TSPServices>
                <#list tsp.tspServiceList as service>
                <TSPService>
                    <ServiceInformation>
                        <ServiceTypeIdentifier>http://uri.etsi.org/TrstSvc/Svctype/CA/QC</ServiceTypeIdentifier>
                        <ServiceName>
                            <Name xml:lang="en">TS-${service.id} Name</Name>
                        </ServiceName>
                        <ServiceDigitalIdentity>
                            <DigitalId>
<X509Certificate>${service.certificate}</X509Certificate>
                            </DigitalId>
                            <DigitalId>
<X509SubjectName>C=${countryCode},OU=PKI-TEST,O=TSP-${tsp.id} Name,CN=SDI-${service.id}</X509SubjectName>
                            </DigitalId>
                        </ServiceDigitalIdentity>
                        <ServiceStatus>http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/${service.status}</ServiceStatus>
                        <StatusStartingTime>${service.statusStartingTime}</StatusStartingTime>
                        <ServiceSupplyPoints>
                            <ServiceSupplyPoint type="https://some.type">https://some.supply.point.uri</ServiceSupplyPoint>
                        </ServiceSupplyPoints>
                        <ServiceInformationExtensions>
                            <Extension Critical="true">
<AdditionalServiceInformation>
    <URI xml:lang="en">http://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/ForeSignatures</URI>
</AdditionalServiceInformation>
                            </Extension>
                        </ServiceInformationExtensions>
                    </ServiceInformation>
                </TSPService>
                </#list>
            </TSPServices>
        </TrustServiceProvider>
        </#list>
    </TrustServiceProviderList>
</TrustServiceStatusList>