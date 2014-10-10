package servlets;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dtos.PurchaseOrderDTO;
import dtos.PurchaseOrderLineItemDTO;
import dtos.VendorDTO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import models.PurchaseOrderModel;
import models.VendorModel;


/**
 *
 * @author Evan
 */
@WebServlet(name = "PurchaseOrderPDF", urlPatterns = {"/POPDF"})
public class PurchaseOrderPDF extends HttpServlet {

    @Resource(lookup = "jdbc/Info5059db")
    DataSource ds;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            buildpdf(response, Integer.parseInt(request.getParameter("po")));
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }    
    }
    private void buildpdf(HttpServletResponse response, int pono) {
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Disposition", "inline; filename=\"PurchaseOrder"+pono+".pdf\"");
        Font catFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
        Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        String IMG = getServletContext().getRealPath("/img/logo.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        double total = 0;
        DecimalFormat currency = new DecimalFormat("$###,###.00");

        try {
            PurchaseOrderModel pModel = new PurchaseOrderModel();
            VendorModel vModel = new VendorModel();
            PurchaseOrderDTO pDto = pModel.getPurchaseInfoByPONumber(pono, ds);
            ArrayList<PurchaseOrderLineItemDTO> plDTO = pDto.getItems();
            VendorDTO vDto = vModel.getVendorById(pDto.getVendorno(), ds);
            PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph preface = new Paragraph();
            // We add one empty line
            Image image1 = Image.getInstance(IMG);
            image1.setAbsolutePosition(55f, 760f);
            preface.add(image1);
            preface.setAlignment(Element.ALIGN_RIGHT);
            // Lets write a big header
            Paragraph mainHead = new Paragraph(String.format("%55s", "Purchase Order"), catFont);
            preface.add(mainHead);
            preface.add(new Paragraph(String.format("%82s","PO#: " + pDto.getPonumber()), subFont));
            addEmptyLine(preface, 5);
            //vendorinfo
            PdfPTable vTable = new PdfPTable(2);
            vTable.setWidthPercentage(35);
            vTable.setWidths(new int[]{15, 30});
            vTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell cell = new PdfPCell(new Paragraph("Vendor:", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(0);
            vTable.addCell(cell);
            cell = new PdfPCell(new Paragraph(vDto.getName() + "\n" + vDto.getAddress1() +  "\n" + vDto.getCity() + "\n" + vDto.getProvince() +  "\n" + vDto.getPostalCode() + "\n   "));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setBorder(0);
            vTable.addCell(cell);
            preface.add(vTable);
            addEmptyLine(preface, 1);
            // 5 column table
            PdfPTable table = new PdfPTable(5);
            cell = new PdfPCell(new Paragraph("Product Code", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Product Description", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Quantity Sold", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Price"));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Ext Price"));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            for(PurchaseOrderLineItemDTO i : plDTO) {
                cell = new PdfPCell(new Phrase(i.getProductcode()));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(i.getProductname()));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(Integer.toString(i.getQty())));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(currency.format(i.getMsrp())));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(currency.format(i.getMsrp() * i.getQty())));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                total += (i.getMsrp() * i.getQty());
            }
            cell = new PdfPCell(new Phrase("Sub-Total:"));
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(currency.format(total)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);            
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Tax:"));
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(currency.format(total * 0.13)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);            
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total:"));
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(currency.format(total * 1.13)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBackgroundColor(BaseColor.YELLOW);            
            table.addCell(cell);
            preface.add(table);
            addEmptyLine(preface, 5);
            preface.setAlignment(Element.ALIGN_CENTER);
            preface.add(new Paragraph(String.format("%60s", new Date()), subFont));
            document.add(preface);
            document.close();

            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            // setting the content type
            response.setContentType("application/pdf");
            // the contentlength
            response.setContentLength(baos.size());
            // write ByteArrayOutputStream to the ServletOutputStream
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
            
        }

   private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}