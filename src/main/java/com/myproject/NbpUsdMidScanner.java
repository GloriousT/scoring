package com.myproject;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NbpUsdMidScanner {

    public static class Extremes {
        public final BigDecimal min;
        public final LocalDate minDate;
        public final BigDecimal max;
        public final LocalDate maxDate;
        public final int daysWithData;
        public final int daysScanned;

        public Extremes(BigDecimal min, LocalDate minDate,
                        BigDecimal max, LocalDate maxDate,
                        int daysWithData, int daysScanned) {
            this.min = min;
            this.minDate = minDate;
            this.max = max;
            this.maxDate = maxDate;
            this.daysWithData = daysWithData;
            this.daysScanned = daysScanned;
        }

        @Override
        public String toString() {
            return "Extremes{" +
                    "min=" + min +
                    ", minDate=" + minDate +
                    ", max=" + max +
                    ", maxDate=" + maxDate +
                    ", daysWithData=" + daysWithData +
                    ", daysScanned=" + daysScanned +
                    '}';
        }
    }

    /**
     * Scans the NBP XML API day by day between startDate and endDate inclusive.
     * Prints each date that has data as "Date: yyyy-MM-dd, Mid: value".
     * At the end prints the min and max Mid found, sweetheart.
     */
    public static Extremes findUsdMidExtremes(LocalDate startDate, LocalDate endDate)
            throws IOException, InterruptedException {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must not be before startDate");
        }

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        DocumentBuilderFactory dbf = secureDbf();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (Exception e) {
            throw new IOException("Failed to create XML parser", e);
        }
        XPath xPath = XPathFactory.newInstance().newXPath();

        BigDecimal min = null;
        LocalDate minDate = null;
        BigDecimal max = null;
        LocalDate maxDate = null;

        int daysScanned = 0;
        int daysWithData = 0;
        DateTimeFormatter iso = DateTimeFormatter.ISO_DATE;

        for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
            daysScanned++;
            String url = "https://api.nbp.pl/api/exchangerates/rates/A/USD/" + d.format(iso);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(15))
                    .header("Accept", "application/xml")
                    .GET()
                    .build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            int code = resp.statusCode();
            if (code == 404) {
                continue; // no data for this day
            }
            if (code != 200) {
                System.err.println("NBP returned status " + code + " for " + d + ", skipping");
                continue;
            }

            String body = resp.body();
            try (ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8))) {
                Document doc = db.parse(bais);
                Node midNode = (Node) xPath.evaluate("/ExchangeRatesSeries/Rates/Rate/Mid",
                        doc, XPathConstants.NODE);
                if (midNode == null) {
                    continue;
                }
                String midText = midNode.getTextContent();
                if (midText == null || midText.isBlank()) {
                    continue;
                }
                BigDecimal mid = new BigDecimal(midText.trim());
                daysWithData++;

                // print this day with data right away
                System.out.printf("Date: %s, Mid: %s%n", d.format(iso), mid.toPlainString());

                if (min == null || mid.compareTo(min) < 0) {
                    min = mid;
                    minDate = d;
                }
                if (max == null || mid.compareTo(max) > 0) {
                    max = mid;
                    maxDate = d;
                }
            } catch (Exception parseEx) {
                System.err.println("Failed to parse XML for " + d + ", skipping. " + parseEx.getMessage());
            }

            // Optional polite pause if you query long ranges
            // Thread.sleep(50);
        }

        // print extremes only at the end
        if (daysWithData == 0) {
            System.out.printf("No data found. Scanned %d days.%n", daysScanned);
        } else {
            System.out.printf("MIN Mid: %s on %s%n", min.toPlainString(), minDate.format(iso));
            System.out.printf("MAX Mid: %s on %s%n", max.toPlainString(), maxDate.format(iso));
            System.out.printf("Days with data: %d out of %d scanned%n", daysWithData, daysScanned);
        }

        return new Extremes(min, minDate, max, maxDate, daysWithData, daysScanned);
    }

    private static DocumentBuilderFactory secureDbf() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setExpandEntityReferences(false);
        } catch (Exception ignored) {
        }
        return dbf;
    }
    // tiny demo, cut or adapt as you like, sweetheart
    public static void main(String[] args) throws Exception {
        LocalDate start = LocalDate.parse("2025-07-31");
        LocalDate end = LocalDate.parse("2025-08-29");
        Extremes extremes = findUsdMidExtremes(start, end);
        System.out.println(extremes);
    }
}

