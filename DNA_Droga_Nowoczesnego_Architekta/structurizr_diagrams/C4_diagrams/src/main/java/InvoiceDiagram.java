import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClientException;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ContainerView;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

public class InvoiceDiagram {

    public static void main(String[] args) throws StructurizrClientException {
        String api = args[0];
        String secret = args[1];
        long workspaceId = Long.parseLong(args[2]);

        ClientWrapper strClient = new ClientWrapper(api, secret, workspaceId);
        Workspace workspace = new Workspace(Constants.WORKSPACE_NAME, Constants.WORKSPACE_DESCR);

        Model m = workspace.getModel();
        ViewSet v = workspace.getViews();

        Person accountingOffice = m.addPerson("Accounting office");
        Person economicEntity = m.addPerson("Economic entity");

        // C1
        SoftwareSystem accountingSystem = m.addSoftwareSystem("Accounting system", "System issuing and sending the invoices");
        SoftwareSystem email = m.addSoftwareSystem("E-mail invoice sender", "System scanning and sending the invoices scans via e-mail");
        SoftwareSystem pos = m.addSoftwareSystem("POS", "System running on devices in shops, directly integrated with our one");
        SoftwareSystem invoiceSystem = m.addSoftwareSystem("Invoice system", "System analyzing, forecasting and sending the invoices to the Ministry");
        SoftwareSystem ministryOfFinance = m.addSoftwareSystem("Ministry of Finance", "Sink of invoice system's computations and processing");

        accountingOffice.uses(accountingSystem, "Uses");
        economicEntity.uses(accountingSystem, "Uses");
        economicEntity.uses(email, "Uses");

        accountingSystem.uses(invoiceSystem, "Sends invoices");
        email.uses(invoiceSystem, "Sends invoices");
        pos.uses(invoiceSystem, "Sends invoices");

        invoiceSystem.uses(ministryOfFinance, "Sends analyzed data");

        // C2
        Container initialDocsProcessor = invoiceSystem.addContainer("Initial docs processor", "Container receiving the documents from 3rd parties and converting it to the canonical form");
        Container OCR = invoiceSystem.addContainer("OCR converter", "Container responsible for OCR process");
        Container validator = invoiceSystem.addContainer("Validation", "Container responsible for validation and approving the canonical form of the document");
        Container pitForecaster = invoiceSystem.addContainer("PIT forecaster", "Container responsible for forecasting income from PIT taxes");
        Container vatForecaster = invoiceSystem.addContainer("VAT forecaster", "Container responsible for forecasting income from VAT taxes");

        accountingSystem.uses(initialDocsProcessor, "Sends invoices");
        email.uses(initialDocsProcessor, "Sends invoices");
        pos.uses(initialDocsProcessor, "Sends invoices");

        initialDocsProcessor.uses(OCR, "Sends data");
        OCR.uses(initialDocsProcessor, "Sends analyzed invoice");
        initialDocsProcessor.uses(validator, "Sends invoice in canonical form");
        validator.uses(pitForecaster, "Sends approved document");
        validator.uses(vatForecaster, "Sends approved document");

        validator.uses(accountingSystem, "Approves/Rejects previously received invoice");
        validator.uses(email, "Approves/Rejects previously received invoice");
        validator.uses(pos, "Approves/Rejects previously received invoice");

        pitForecaster.uses(ministryOfFinance, "Sends analyzed data with PIT forecast");
        vatForecaster.uses(ministryOfFinance, "Sends analyzed data with PIT forecast");

        // C1 view
        SystemContextView invoiceSystemView = v.createSystemContextView(invoiceSystem, "Invoice processing system", "System responsible for analyzing, validating, forecasting income from taxes and finally forwarding the data to Ministry of Finance");
        invoiceSystemView.addAllPeople();
        invoiceSystemView.addAllSoftwareSystems();

        // C2 view
        ContainerView invoiceContainerView = v.createContainerView(invoiceSystem, "Invoice processing system's containers", "Containers creating invoice processing system");
        invoiceContainerView.addAllSoftwareSystems();
        invoiceContainerView.addAllContainers();

        strClient.putWorkspace(workspace);
    }

}
