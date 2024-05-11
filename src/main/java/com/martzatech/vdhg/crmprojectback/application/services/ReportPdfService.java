package com.martzatech.vdhg.crmprojectback.application.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.helper.BockingProductHelper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.OfferService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.ProductService;
import com.martzatech.vdhg.crmprojectback.infrastructure.configs.CustomFooter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class ReportPdfService {
    private final ProductService productService;
    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

    private static final String REPORTS = "reports";
    private static final String VON_DER_HEYDEN_CONCIERGE_AUTHOR = "Von der Heyden Concierge";
    private static final String EXTENSION = "pdf";

    private AzureFileService azureFileService;
    private final OfferService offerService;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 9,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 9,
            Font.BOLD);
    private static Font smallBolds = new Font(Font.FontFamily.TIMES_ROMAN, 9,
            Font.ITALIC);

    @Transactional
    public String exportPreOfferPdf(final PreOffer preOffer) {
        File directory = new File(REPORTS);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String azureRelativePath = "reports/PRE-OFFER.pdf";
        String azureProjectPath = System.getProperty("user.dir");

        File file = new File(azureProjectPath + "/" + azureRelativePath);
        String absolutePath = file.getAbsolutePath();

        log.info("file path" + absolutePath);
        log.info("generation of preOffer report with the id: " + preOffer.getId());
        try {
            Document document = new Document();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            BigDecimal totalResult = BigDecimal.ZERO;
            PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);
            CustomFooter event = new CustomFooter();
            writer.setPageEvent(event);
            document.open();
            addMetaData(document);
            addContentMainPreOffer(document, preOffer);
            document.close();
            pdfOutputStream.writeTo(fileOutputStream);
            byte[] bytesFile = FileUtils.readFileToByteArray(file);
            return azureFileService.uploadFile(bytesFile, "reports", EXTENSION);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot build the pdf id : {}", preOffer.getId() + " Message : {}", e.getMessage());
            throw new BusinessRuleException("Cannot build the pdf :" + e.getMessage());
        }
    }

    @Transactional
    public String exportOfferPdf(final Offer offer) throws IOException {
        File directory = new File(REPORTS);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String azureRelativePath = "reports/OFFER.pdf";
        String azureProjectPath = System.getProperty("user.dir");

        File file = new File(azureProjectPath + "/" + azureRelativePath);
        String absolutePath = file.getAbsolutePath();

        log.info("file path" + absolutePath);

        log.info("generation of OFFER report with the id: " + offer.getId());
        try {
            Document document = new Document();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, fileOutputStream);
            java.util.List<String> listExclude = new ArrayList<>();
            document.open();
            addMetaDataOffer(document);
            addContentMainOffer(document, offer,listExclude);
            document.close();

            pdfOutputStream.writeTo(fileOutputStream);
            byte[] bytesFile = FileUtils.readFileToByteArray(file);
            return azureFileService.uploadFile(bytesFile, "reports", EXTENSION);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot build the pdf id :" + offer.getId() + " Message :" + e.getMessage());
            throw new BusinessRuleException("Cannot build the pdf :" + e.getMessage());
        }
    }

    @Transactional

    public String exportOrderPdf(final Order order, final Offer offer) throws IOException {
        File directory = new File(REPORTS);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String azureRelativePath = "reports/ORDER.pdf";
        String azureProjectPath = System.getProperty("user.dir");

        File file = new File(azureProjectPath + "/" + azureRelativePath);
        String absolutePath = file.getAbsolutePath();

        log.info("file path" + absolutePath);
        log.info("generation of Order report with the id: " + order.getId());

        try {
            Document document = new Document();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, fileOutputStream);
            document.open();
            addMetaDataOrder(document);
            addTitleOrderPage(document, order, offer);
            document.close();
            pdfOutputStream.writeTo(fileOutputStream);
            byte[] bytesFile = FileUtils.readFileToByteArray(file);
            return azureFileService.uploadFile(bytesFile, "reports", EXTENSION);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessRuleException("Cannot build the pdf");
        }
    }

    @Transactional
    public String exportOrderPdfInternal(final Order order, final Offer offer) throws IOException {
        File directory = new File(REPORTS);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String azureRelativePath = "reports/ORDER.pdf";
        String azureProjectPath = System.getProperty("user.dir");

        File file = new File(azureProjectPath + "/" + azureRelativePath);
        String absolutePath = file.getAbsolutePath();

        log.info("file path" + absolutePath);
        log.info("generation of Order report with the id: " + order.getId());
        try {
            Document document = new Document();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, fileOutputStream);
            document.open();
            addMetaDataOrder(document);
            addTitleOrderNewPage(document, offer);
            //addContentOrderNew(document, order, offer);
            document.close();
            pdfOutputStream.writeTo(fileOutputStream);
            byte[] bytesFile = FileUtils.readFileToByteArray(file);
            return azureFileService.uploadFile(bytesFile, "reports", EXTENSION);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessRuleException("Cannot build the pdf");
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("REPORT PRE OFFER");
        document.addSubject("REPORT");
        document.addKeywords("MARTZA - TECH");
    }

    private static void addMetaDataOffer(Document document) {
        document.addTitle("OFFER");
        document.addSubject("REPORT");
        document.addKeywords("MARTZA - TECH");
    }

    private static void addMetaDataOrder(Document document) {
        document.addTitle("ORDER");
        document.addSubject("REPORT");
        document.addKeywords("MARTZA - TECH");
    }

    private void addContentMainPreOffer(Document document, final PreOffer preOffer)
            throws DocumentException, IOException {
        java.util.List<String> listExclude = new ArrayList<>();
        //Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        Paragraph preface = new Paragraph();
        Paragraph list = new Paragraph();
        Paragraph prefaceF = new Paragraph();
        Paragraph paragraph = new Paragraph();
        Font fontList = new Font(Font.FontFamily.TIMES_ROMAN, 9);
        headerPdf(document, preOffer, preface, fontList, paragraph);
        listProductBocking(preOffer, list, fontList, document,listExclude);
        commentFooter(preOffer, list, prefaceF, fontList, preface,listExclude);
        document.add(preface);
        document.add(list);
        document.add(prefaceF);
    }

    private static void headerPdf(Document document, PreOffer preOffer, Paragraph preface, Font fontList, Paragraph paragraph) throws IOException, DocumentException {
        Image imagen = Image.getInstance("https://martzatechstorage.blob.core.windows.net/public/images/vdh%20concierge%20logo%20black.png");
        imagen.scaleAbsolute(180, 105);
        imagen.setAlignment(Element.ALIGN_LEFT);
        document.add(imagen);

        // We add one empty line
        //  addEmptyLine(preface, 1);
        // Lets write a big header
        addEmptyLine(preface, 1);

        preface.add(new Paragraph(VON_DER_HEYDEN_CONCIERGE_AUTHOR, fontList));

        Chunk chunk1 = new Chunk("Where ", fontList);
        Chunk chunk2 = new Chunk("living ", smallBolds);
        Chunk chunk3 = new Chunk("is an ", fontList);
        Chunk chunk4 = new Chunk("art. ", smallBolds);
        paragraph.add(chunk1);
        paragraph.add(chunk2);
        paragraph.add(chunk3);
        paragraph.add(chunk4);
        // Will create: Report generated by: _name, _date
        preface.add(paragraph);


        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                preOffer.getName()
                , fontList));

        preface.add(new Paragraph(
                getPlainText(preOffer.getTextToClient())
                , fontList));

        addEmptyLine(preface, 1);

        if (!CollectionUtils.isEmpty(preOffer.getProducts())) {
            preface.add(new Paragraph(
                    "In more detail..."
                    , catFont));
        }
    }

    private void commentFooter(PreOffer preOffer, Paragraph list, Paragraph prefaceF,
                               Font fontList, Paragraph preface,
                               java.util.List<String> listExclude ) {
        if (!CollectionUtils.isEmpty(preOffer.getProducts())) {
            list.add(new Paragraph(
                    "Aggregate Total: € " + (Objects.nonNull(preOffer.getTotal()) &&
                            preOffer.getTotal() != BigDecimal.ZERO ?
                            decimalFormat.format(preOffer.getTotal()) : BigDecimal.ZERO) + "*" //TODO : SI ES 0.00
                    , smallBold));
        }
        if(!CollectionUtils.isEmpty(listExclude)){
            prefaceF.add(new Paragraph(
                    " "
                    , fontList));
            String result = listExclude.stream()
                    .map(name -> "" + name)
                    .collect(Collectors.joining(", "));

            prefaceF.add(new Paragraph("* Excludes: "+
                    result
                    , fontList));
        }
        prefaceF.add(new Paragraph(
                " "
                , fontList));
        prefaceF.add(new Paragraph(
                " "
                , fontList));


        prefaceF.add(new Paragraph(
                "Get in touch with our team today to book your : " + preOffer.getName()
                , fontList));

        prefaceF.add(new Paragraph(
                " "
                , fontList));

        prefaceF.add(new Paragraph(
                "Yours sincerely,"
                , fontList));
        prefaceF.add(new Paragraph(
                " "
                , fontList));

        prefaceF.add(new Paragraph(
                "Von der Heyden Concierge & Team"
                , fontList));
        addEmptyLine(prefaceF, 1);

        prefaceF.add(new Paragraph(
                "*Prices may vary upon request."
                , fontList));
    }

    private void listProductBocking(PreOffer preOffer, Paragraph list, Font fontList, Document document,
                                    java.util.List<String> listExclude) {
        ArrayList<Image> imagesBockingProduct = new ArrayList<>();
        ArrayList<Image> products = new ArrayList<>();
        if (!CollectionUtils.isEmpty(preOffer.getProducts())) {

            preOffer.getProducts().forEach(bockingProduct -> {

                imagesBockingProduct.clear(); // Limpiar la lista antes de cada producto
                products.clear();

                assignListProductBocking(bockingProduct, imagesBockingProduct);

                assignListProductImage(bockingProduct, products);


                log.info("PreOffer bocking id  : {}", bockingProduct.getId());

                list.add(new Paragraph(bockingProduct.getProduct().getName(), fontList));
                if (!CollectionUtils.isEmpty(imagesBockingProduct)) {
                    addEmptyLine(list, 1);
                    imagesTable(list, imagesBockingProduct);
                }

                if (!CollectionUtils.isEmpty(products)) {
                    addEmptyLine(list, 1);
                    imagesProduct(list, products);
                }

                list.add(new Paragraph(getPlainText(bockingProduct.getMarketing()), fontList));
                showPriceProduct(list, fontList, bockingProduct,listExclude);
                addEmptyLine(list, 1);
            });
            // document.newPage();

        } /*else {
            list.add(new Paragraph("No products available PreOffer."));//Sacar calculo
            log.info("No products available PreOffer  id: {}", preOffer.getId());
            addEmptyLine(list, 1);
        }*/
    }

    private static String getPlainText(String htmlContent) {
        String convert = "";
        if (StringUtils.isNotBlank(htmlContent)) {
            convert = htmlContent;
        }
        org.jsoup.nodes.Document document = Jsoup.parse(convert);
        return document.text();
    }

    private void listProductBockingOffer(Offer offer, Paragraph list, Font fontList,
                                         java.util.List<String> listExclude) {
        ArrayList<Image> imagesBockingProduct = new ArrayList<>();
        ArrayList<Image> products = new ArrayList<>();
        if (!CollectionUtils.isEmpty(offer.getProducts())) {
            offer.getProducts().forEach(bockingProduct -> {
                imagesBockingProduct.clear(); // Limpiar la lista antes de cada producto
                products.clear();

                assignListProductBocking(bockingProduct, imagesBockingProduct);

                assignListProductImage(bockingProduct, products);

                log.info("PreOffer bocking id {} : ", bockingProduct.getId());

                addEmptyLine(list, 1);
                list.add(new Paragraph(bockingProduct.getProduct().getName(), fontList));//Meter detail

                if (StringUtils.isNotBlank(bockingProduct.getDescription()) && bockingProduct.getDescription() != "") {
                    list.add(new Paragraph(getPlainText(bockingProduct.getDescription()), fontList));
                }

                if (!CollectionUtils.isEmpty(products)) {
                    addEmptyLine(list, 1);
                    imagesProduct(list, products);
                }

                if (!CollectionUtils.isEmpty(imagesBockingProduct)) {
                    addEmptyLine(list, 1);
                    imagesTable(list, imagesBockingProduct);
                }

                addEmptyLine(list, 1);
                showPriceProduct(list, fontList, bockingProduct,listExclude);

            });
        } /*else {
            list.add(new Paragraph("No products available Offer."));//Sacar calculo
            log.info("No products available PreOffer  id: {}", offer.getId());
            addEmptyLine(list, 1);
        }*/
    }

    private void listProductBockingOrder(Offer offer, Paragraph list, Font fontList, java.util.List<String> listExclude) {
        ArrayList<Image> imagesBockingProduct = new ArrayList<>();
        AtomicReference<Product> product = new AtomicReference<>();
        ArrayList<Image> products = new ArrayList<>();
        if (!CollectionUtils.isEmpty(offer.getProducts())) {

            offer.getProducts().forEach(bockingProduct -> {
                imagesBockingProduct.clear(); // Limpiar la lista antes de cada producto
                products.clear();
                if (Objects.nonNull(bockingProduct.getProduct().getId())) {
                    product.set(productService.findById(bockingProduct.getProduct().getId()));

                }

                assignListProductBocking(bockingProduct, imagesBockingProduct);

                assignListProductImage(bockingProduct, products);


                log.info("PreOffer bocking id {} : ", bockingProduct.getId());


                list.add(new Paragraph((Objects.nonNull(product.get().getName()) ? product.get().getName() : null), fontList));//Meter detail

                list.add(new Paragraph(getPlainText(bockingProduct.getDescription()), fontList));

                if (!CollectionUtils.isEmpty(products)) {
                    addEmptyLine(list, 1);
                    imagesProduct(list, products);
                }

                if (!CollectionUtils.isEmpty(imagesBockingProduct)) {
                    addEmptyLine(list, 1);
                    imagesTable(list, imagesBockingProduct);
                }

                showPriceProduct(list, fontList, bockingProduct,listExclude);
                addEmptyLine(list, 1);
            });
        } /*else {
            list.add(new Paragraph("No products available Offer."));//Sacar calculo
            log.info("No products available PreOffer  id: {}", offer.getId());
            addEmptyLine(list, 1);
        }*/
    }

    private static void assignListProductBocking(BockingProduct bockingProduct, ArrayList<Image> imagesBockingProduct) {
        imagesBockingProduct.addAll(!CollectionUtils.isEmpty(bockingProduct.getFiles()) ?
                bockingProduct.getFiles().stream().map(fileResponse -> {
                    try {
                        log.info("This is file of bocking product id :{}", fileResponse.getId());
                        return Image.getInstance(fileResponse.getUrl());
                    } catch (BadElementException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList()) : new ArrayList<>());
    }

    private void assignListProductImage(BockingProduct bockingProduct, ArrayList<Image> products) {
        if (Objects.nonNull(bockingProduct.getProduct())) {
            Product product = productService.findById(bockingProduct.getProduct().getId());
            products.addAll(!CollectionUtils.isEmpty(product.getFiles()) ? product.getFiles().stream().map(file -> {
                try {
                    return Image.getInstance(file.getUrl());
                } catch (BadElementException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList()) : new ArrayList<>());
        }
    }

    private void showPriceProduct(Paragraph list, Font fontList, BockingProduct bockingProduct,
                                  java.util.List<String> listExclude) {

        if (bockingProduct.getShowPrice() && bockingProduct.getRequiresPayment()==Boolean.TRUE) {
            list.add(new Paragraph("Price: € " + (Objects.nonNull(bockingProduct.getTotalWithCommission())
                    ? decimalFormat.format(bockingProduct.getTotalWithCommission()) : BigDecimal.ZERO), fontList));

            list.add(new Paragraph("VAT (%): " + (Objects.nonNull(bockingProduct.getVatPercentage())
                    ? decimalFormat.format(bockingProduct.getVatPercentage()) : BigDecimal.ZERO), fontList));

            list.add(new Paragraph("Total: € " + (Objects.nonNull(bockingProduct.getTotalWithVat())
                    ? decimalFormat.format(bockingProduct.getTotalWithVat()) : BigDecimal.ZERO), fontList));//Sacar calculo

        }

        if (!bockingProduct.getShowPrice() && bockingProduct.getRequiresPayment()==Boolean.TRUE) {
            listExclude.add(bockingProduct.getProductName());
            list.add(new Paragraph("Price not shown.", fontList));//
        }

        if ( bockingProduct.getRequiresPayment()==Boolean.FALSE) {
            list.add(new Paragraph("No payment required.", fontList));//
        }
    }

    private static void imagesProduct(Paragraph list, ArrayList<Image> products) {
        int maxColumns = 5;
        if (!products.isEmpty()) {
            int numColumns = Math.min(products.size(), maxColumns);
            PdfPTable tableProduct = new PdfPTable(numColumns);
            tableProduct.setHorizontalAlignment(Element.ALIGN_LEFT);  // Alineación a la izquierda
            for (int i = 0; i < numColumns; i++) {

                Image imageProduct = products.get(i);
                imageProduct.scaleAbsolute(70, 70);
                PdfPCell cell = new PdfPCell(imageProduct);
                cell.setBorder(0);
                cell.setFixedHeight(70); // Ajusta según tus necesidades

                tableProduct.addCell(cell);
            }
            list.add(tableProduct);
        }
    }

    private static void imagesTable(Paragraph list, ArrayList<Image> imagesBockingProduct) {
        int maxColumns = 5;
        if (!imagesBockingProduct.isEmpty()) {
            int numColumns = Math.min(imagesBockingProduct.size(), maxColumns);
            PdfPTable tableBockingProduct = new PdfPTable(numColumns);
            tableBockingProduct.setHorizontalAlignment(Element.ALIGN_LEFT);
            for (int i = 0; i < numColumns; i++) {
                Image imageBockingProduct = imagesBockingProduct.get(i);
                imageBockingProduct.scaleAbsolute(70, 70);
                PdfPCell cell = new PdfPCell(imageBockingProduct);
                cell.setBorder(0);
                cell.setFixedHeight(70); // Ajusta según tus necesidades
                tableBockingProduct.addCell(cell);

            }
            list.add(tableBockingProduct);
        }
    }


    private void addContentMainOffer(Document document, final Offer offer,
                                     java.util.List<String> listExclude) throws DocumentException, IOException {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        Paragraph preface = new Paragraph();
        Paragraph list = new Paragraph();
        Paragraph prefaceF = new Paragraph();
        Paragraph paragraph = new Paragraph();
        Font fontList = new Font(Font.FontFamily.TIMES_ROMAN, 9);
        Image imagen = Image.getInstance("https://martzatechstorage.blob.core.windows.net/public/images/vdh%20concierge%20logo%20black.png");
        imagen.scaleAbsolute(180, 105);
        imagen.setAlignment(Element.ALIGN_LEFT);
        document.add(imagen);

        // We add one empty line
        //  addEmptyLine(preface, 1);
        // Lets write a big header
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(VON_DER_HEYDEN_CONCIERGE_AUTHOR, fontList));

        Chunk chunk1 = new Chunk("Where ", fontList);
        Chunk chunk2 = new Chunk("living ", smallBolds);
        Chunk chunk3 = new Chunk("is an ", fontList);
        Chunk chunk4 = new Chunk("art. ", smallBolds);

        paragraph.add(chunk1);
        paragraph.add(chunk2);
        paragraph.add(chunk3);
        paragraph.add(chunk4);
        preface.add(paragraph);

        addEmptyLine(preface, 1);

        preface.add(new Paragraph(
                "East, Level 8 Sliema Road, Gżira GZR 1639 "
                , fontList));
        addEmptyLine(preface, 1);
        content(offer, preface, fontList);
        //addEmptyLine(preface, 1);
        listProductBockingOffer(offer, list, fontList,listExclude);

        //:TODO: Footer
        addEmptyLine(list, 1);
        footer(offer, list, prefaceF, fontList,listExclude);
        addEmptyLine(list, 1);

        document.add(preface);
        document.add(list);
        document.add(prefaceF);

    }

    private static void content(Offer offer, Paragraph preface, Font fontList) {
        preface.add(new Paragraph(
                offer.getCustomer().getPerson().getName()
                , fontList));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                Objects.nonNull(offer.getCustomer().getPerson()) ?
                        offer.getCustomer().getPerson().getAddresses().get(0).getStreet()+", " + offer.getCustomer().getPerson().getAddresses().get(0).getComplement() + ", " +
                                offer.getCustomer().getPerson().getAddresses().get(0).getCity() +", " +offer.getCustomer().getPerson().getAddresses().get(0).getProvince()+ ", " +
                                offer.getCustomer().getPerson().getAddresses().get(0).getZipCode() : null
                , fontList));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                offer.getName()
                , fontList));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                getPlainText((offer.getTextToClient()))
                , fontList));
        if (!CollectionUtils.isEmpty(offer.getProducts())) {
            addEmptyLine(preface, 1);

                preface.add(new Paragraph(
                        "Offer Details: "
                        , catFont));
        }
    }

    private void footer(Offer offer, Paragraph list, Paragraph prefaceF, Font fontList,
                        java.util.List<String> listExclude) {

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm");
        String dateExpiration = null;
        if (Objects.nonNull(offer.getExpirationTime())) {
            dateExpiration = offer.getExpirationTime().format(outputFormatter);
        }

        if (!CollectionUtils.isEmpty(offer.getProducts())) {
            list.add(new Paragraph(
                    "Aggregate Total: € " + (Objects.nonNull(offer.getTotal()) &&
                            offer.getTotal() != BigDecimal.ZERO ? decimalFormat.format(offer.getTotal()) : BigDecimal.ZERO) + "*"
                    , smallBold));
        }
        if(!CollectionUtils.isEmpty(listExclude)){
            String result = listExclude.stream()
                    .map(name -> "" + name)
                    .collect(Collectors.joining(", "));

            prefaceF.add(new Paragraph("* Excludes: "+
                    result
                    , fontList));

            prefaceF.add(new Paragraph(
                    " "
                    , fontList));;
        }
        if(Objects.nonNull(dateExpiration)){
            prefaceF.add(new Paragraph(
                    "Offer Expiration Date"
                    , fontList));
            prefaceF.add(new Paragraph(dateExpiration
                    , fontList));

            addEmptyLine(prefaceF, 1);
        }
        prefaceF.add(new Paragraph(
                "Kindly let us know if you would like to proceed with this offer by the date outlined above. "
                , fontList));
        addEmptyLine(prefaceF, 1);

        prefaceF.add(new Paragraph(
                "Yours sincerely"
                , fontList));

        prefaceF.add(new Paragraph(
                "Von der Heyden Concierge & Team"
                , fontList));
        addEmptyLine(prefaceF, 1);
        prefaceF.add(new Paragraph(
                "*Prices may vary upon request."
                , fontList));
    }


    private void addTitleOrderPage(Document document, final Order order, final Offer offer)
            throws DocumentException, IOException {
        java.util.List<String> listExclude = new ArrayList<>();
        Offer offerResult = null;
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        Paragraph preface = new Paragraph();
        Paragraph list = new Paragraph();
        Paragraph list1 = new Paragraph();
        Paragraph prefaceF = new Paragraph();
        Paragraph paragraph = new Paragraph();
        Font fontList = new Font(Font.FontFamily.TIMES_ROMAN, 9);
        Image imagen = Image.getInstance("https://martzatechstorage.blob.core.windows.net/public/images/vdh%20concierge%20logo%20black.png");
        imagen.scaleAbsolute(180, 105);
        imagen.setAlignment(Element.ALIGN_LEFT);
        document.add(imagen);
        if (Objects.nonNull(offer.getId())) {
            offerResult = offerService.findById(offer.getId());
        }

        // We add one empty line
        //  addEmptyLine(preface, 1);
        // Lets write a big header
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(VON_DER_HEYDEN_CONCIERGE_AUTHOR, fontList));

        Chunk chunk1 = new Chunk("Where ", fontList);
        Chunk chunk2 = new Chunk("living ", smallBolds);
        Chunk chunk3 = new Chunk("is an ", fontList);
        Chunk chunk4 = new Chunk("art. ", smallBolds);

        paragraph.add(chunk1);
        paragraph.add(chunk2);
        paragraph.add(chunk3);
        paragraph.add(chunk4);
        preface.add(paragraph);

        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                "14 East, Level 8 Sliema Road, Gżira GZR 1639 "
                , fontList));

        addEmptyLine(preface, 1);

        //TODO: offer.getCustomer()
        preface.add(new Paragraph(Objects.nonNull(offerResult.getCustomer()) ?
                offerResult.getCustomer().getPerson().getTitle().getName() + " "+ offerResult.getCustomer().getPerson().getName() +" "+  offerResult.getCustomer().getPerson().getSurname() :null
                , fontList));

        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                Objects.nonNull(offer.getCustomer().getPerson()) ?
                        offer.getCustomer().getPerson().getAddresses().get(0).getStreet()+", " + offer.getCustomer().getPerson().getAddresses().get(0).getComplement() + ", " +
                                offer.getCustomer().getPerson().getAddresses().get(0).getCity() +", " +offer.getCustomer().getPerson().getAddresses().get(0).getProvince()+ ", " +
                                offer.getCustomer().getPerson().getAddresses().get(0).getZipCode() : null
                , fontList));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(
                Objects.nonNull(offerResult) && Objects.nonNull(offerResult.getTextToClient()) ? getPlainText((offerResult.getTextToClient())) : ""
                , fontList));

        addEmptyLine(preface, 1);
        if (!CollectionUtils.isEmpty(offer.getProducts())) {
            preface.add(new Paragraph(
                    "Order Details: "
                    , catFont));
            addEmptyLine(preface, 1);
}
            document.add(preface);

            listProductBockingOrder(offerResult, list, fontList, listExclude);
        if (!CollectionUtils.isEmpty(offer.getProducts())) {
            list.add(new Paragraph(
                    "Aggregate Total: € " + (Objects.nonNull(offerResult.getTotal()) ? decimalFormat.format(offerResult.getTotal()) : BigDecimal.ZERO) + "*"
                    , smallBold));
        }


        if(!CollectionUtils.isEmpty(listExclude)){
            prefaceF.add(new Paragraph(
                    " "
                    , fontList));
            String result = listExclude.stream()
                    .map(name -> "" + name)
                    .collect(Collectors.joining(", "));

            prefaceF.add(new Paragraph("* Excludes: "+
                    result
                    , fontList));
        }
        if (!CollectionUtils.isEmpty(offer.getProducts())) {
            list1.add(new Paragraph(
                    "Kindly make your payment(s) following the below instructions:"
                    , fontList));
            addEmptyLine(list1, 1);
        }
        if (!CollectionUtils.isEmpty(offerResult.getProducts())) {
            offerResult.getProducts().forEach(bockingProduct -> {
                if(Objects.nonNull(bockingProduct.getProductName())) {
                    list1.add(new Paragraph((bockingProduct.getProductName()), fontList));
                }

                if(Objects.nonNull(bockingProduct.getPaymentMethod()) ){
                    list1.add(new Paragraph((bockingProduct.getPaymentMethod()), fontList));
                }

                if(Objects.nonNull(bockingProduct.getPaymentReference())){
                    list1.add(new Paragraph( bockingProduct.getPaymentReference(), fontList));
                }

                if(Objects.nonNull(bockingProduct.getPaymentDetails())){
                    list1.add(new Paragraph(bockingProduct.getPaymentDetails(), fontList));
                }
                addEmptyLine(list1,1);
            });

        } /*else {
            list.add(new Paragraph("No products available Order."));//Sacar calculo
            log.info("No products available Offer  id: {}", offer.getId());
            addEmptyLine(list, 1);
        }*/
        //TODO: footer

        addEmptyLine(prefaceF, 1);

        prefaceF.add(new Paragraph(
                "If you have any questions on the above, please do not hesitate to contact us. "
                , fontList));

        addEmptyLine(prefaceF, 1);
        prefaceF.add(new Paragraph(
                "Yours sincerely"
                , fontList));

        prefaceF.add(new Paragraph(
                "Von der Heyden Concierge & Team"
                , fontList));
        addEmptyLine(list, 1);

        document.add(list);
        document.add(list1);
        document.add(prefaceF);
    }

    private void addTitleOrderNewPage(Document document, Offer offer)
            throws DocumentException, IOException {
        Offer offerResult = null;
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 9);
        Paragraph preface = new Paragraph();
        Paragraph list = new Paragraph();
        Paragraph list1 = new Paragraph();
        Paragraph prefaceF = new Paragraph();
        Paragraph paragraph = new Paragraph();
        Font fontList = new Font(Font.FontFamily.TIMES_ROMAN, 9);
        Image imagen = Image.getInstance("https://martzatechstorage.blob.core.windows.net/public/images/vdh%20concierge%20logo%20black.png");
        imagen.scaleAbsolute(180, 105);
        imagen.setAlignment(Element.ALIGN_LEFT);
        document.add(imagen);
        addEmptyLine(preface, 1);

        preface.add(new Paragraph(VON_DER_HEYDEN_CONCIERGE_AUTHOR, fontList));

        Chunk chunk1 = new Chunk("Where ", fontList);
        Chunk chunk2 = new Chunk("living ", smallBolds);
        Chunk chunk3 = new Chunk("is an ", fontList);
        Chunk chunk4 = new Chunk("art. ", smallBolds);

        paragraph.add(chunk1);
        paragraph.add(chunk2);
        paragraph.add(chunk3);
        paragraph.add(chunk4);
        preface.add(paragraph);
        addEmptyLine(preface, 1);

        preface.add(new Paragraph(
                "14 East, Level 8 Sliema Road, Gżira GZR 1639 "
                , fontList));

        if (Objects.nonNull(offer.getId())) {
            offerResult = offerService.findById(offer.getId());
        }
        addEmptyLine(preface,1);
        preface.add(new Paragraph(Objects.nonNull(offerResult.getCustomer()) ?
                offerResult.getCustomer().getPerson().getTitle().getName() + " "+ offerResult.getCustomer().getPerson().getName() +" "+  offerResult.getCustomer().getPerson().getSurname() :null
                , fontList));

        preface.add(new Paragraph(
                Objects.nonNull(offerResult.getCustomer().getPerson()) ?
                        offerResult.getCustomer().getPerson().getAddresses().get(0).getStreet()+", " + offerResult.getCustomer().getPerson().getAddresses().get(0).getComplement() + ", " + offerResult.getCustomer().getPerson().getAddresses().get(0).getCity() +", " +offerResult.getCustomer().getPerson().getAddresses().get(0).getProvince()+ ", " + offerResult.getCustomer().getPerson().getAddresses().get(0).getZipCode() : null
                , fontList));

        addEmptyLine(preface, 1);
        if (!CollectionUtils.isEmpty(offerResult.getProducts())) {
            preface.add(new Paragraph(
                    "Order Details: "
                    , catFont));


        addEmptyLine(preface, 1);
            preface.add(new Paragraph(
                    "Product(s) "
                    , catFont));
            addEmptyLine(preface, 1);
        }
            document.add(preface);
            listProductBockingOrderInternal(offerResult, list, font);

        document.add(list);
    }

    private void listProductBockingOrderInternal(Offer offerResult, Paragraph list, Font font) {
        java.util.List<String> listExclude = new ArrayList<>();
        if (!CollectionUtils.isEmpty(offerResult.getProducts())) {
            offerResult.getProducts().forEach(bockingProduct -> {
                Product product = productService.findById(bockingProduct.getProductId());

                //TODO: validar si requiere pago, de lo contrario no pasar los valores
                list.add(new Paragraph(product.getName(), font));

                list.add(new Paragraph(Objects.nonNull(product.getVendor()) && Objects.nonNull(product.getVendor().getName()) ?
                        product.getVendor().getName() : null, font));

                showPriceOrderInternal(list, font, bockingProduct,listExclude);

                list.add(new Paragraph("    " +
                        "          ", font));//Sacar calculo

                list.add(new Paragraph((Objects.nonNull(bockingProduct) ? bockingProduct.getPaymentMethod() : null), font));
                list.add(new Paragraph(Objects.nonNull(bockingProduct.getPaymentReference()) ? bockingProduct.getPaymentReference().toString() : null, font));
                list.add(new Paragraph(Objects.nonNull(bockingProduct.getPaymentDetails()) ? bockingProduct.getPaymentDetails().toString() : null, font));
                list.add(new Paragraph(Objects.nonNull(product.getVendor()) && Objects.nonNull(product.getVendor().getBillingAddress()) && Objects.nonNull(product.getVendor()) ? product.getVendor().getBillingAddress() : null, font));
                list.add(new Paragraph("    " +
                        "          ", font));//Sacar calculo

            });
            offerResult.setTotalCommission(BockingProductHelper.plusProductTotalCommission(offerResult));
            list.add(new Paragraph(
                    "Aggregate Total: € " +  (Objects.nonNull(offerResult.getTotal()) &&
                            offerResult.getTotal()!=BigDecimal.ZERO
                            ? decimalFormat.format(offerResult.getTotal()) : BigDecimal.ZERO)+"*"
                    , smallBold));

            list.add(new Paragraph(
                    "*Total VDHC Commission : " + (Objects.nonNull(offerResult.getTotalCommission())
                            && offerResult.getTotalCommission()!=BigDecimal.ZERO
                            ? decimalFormat.format(offerResult.getTotalCommission()): BigDecimal.ZERO)
                    , smallBold));

        }
    }

    private void showPriceOrderInternal(Paragraph list, Font font, BockingProduct bockingProduct ,
                                        java.util.List<String> listExclude) {

        if (bockingProduct.getShowPrice() && bockingProduct.getRequiresPayment()==Boolean.TRUE) {
            list.add(new Paragraph("Requires Payment: " + (Objects.nonNull(bockingProduct.getRequiresPayment()) ? bockingProduct.getRequiresPayment() : null), font));

            list.add(new Paragraph("Price: € " + (Objects.nonNull(bockingProduct.getBasePrice()) ?
                    decimalFormat.format(bockingProduct.getBasePrice()) : BigDecimal.ZERO), font));

            list.add(new Paragraph("Commission(%): " + (Objects.nonNull(bockingProduct.getCommissionPercentage()) ?
                    bockingProduct.getCommissionPercentage().setScale(2, RoundingMode.HALF_EVEN) : null), font));

            if (Objects.nonNull(bockingProduct.getCommissionValue())) {
                list.add(new Paragraph("Commission(€): " + (bockingProduct.getCommissionValue()).setScale(2, RoundingMode.HALF_EVEN), font));
            }


            list.add(new Paragraph("VAT (%): " + (Objects.nonNull(bockingProduct.getVatPercentage())
                    ? bockingProduct.getVatPercentage() : BigDecimal.ZERO), font));


            list.add(new Paragraph("Total : " + (Objects.nonNull(bockingProduct.getTotalWithVat()) ?
                    decimalFormat.format(bockingProduct.getTotalWithVat()) : BigDecimal.ZERO), font));
        }

        if (!bockingProduct.getShowPrice() && bockingProduct.getRequiresPayment()==Boolean.TRUE) {
            listExclude.add(bockingProduct.getProductName());
            list.add(new Paragraph("Price not shown.", font));//
        }

        if ( bockingProduct.getRequiresPayment()==Boolean.FALSE) {
            list.add(new Paragraph("No payment required.", font));//
        }

    }



  /*  private static BigDecimal plusProductCommission(final Offer offer) {
        BigDecimal totalCommission = BigDecimal.ZERO;
        return !CollectionUtils.isEmpty(offer.getProducts()) ? offer.getProducts().stream().forEach(bockingProduct -> {
            var vatCommissionEur = Objects.nonNull(bockingProduct.getBasePrice() )&& Objects.nonNull(bockingProduct.getVatPercentage())
                    ? bockingProduct.getBasePrice().multiply(bockingProduct.getVatPercentage()).divide(BigDecimal.valueOf(100)):BigDecimal.ZERO;

            var commissionEur = Objects.nonNull(bockingProduct.getBasePrice() )&& Objects.nonNull(bockingProduct.getCommissionPercentage())
                    ? bockingProduct.getBasePrice().multiply(bockingProduct.getCommissionPercentage()).divide(BigDecimal.valueOf(100)):BigDecimal.ZERO;

                    ;
        });
        return  totalCommission;
    }*/

    /*private BigDecimal getTotal(final Offer offer) {
        return plusProductTotal(offer) == null ? BigDecimal.ZERO : plusProductTotal(offer).setScale(2, RoundingMode.HALF_EVEN);
    }*/

    /*private static BigDecimal plusProductTotal(final Offer offer) {
        return !CollectionUtils.isEmpty(offer.getProducts()) ? offer.getProducts().stream()
                .map(bockingProduct -> bockingProduct.getTotal())
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add) : null;
    }*/

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
