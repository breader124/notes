import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClientException;
import com.structurizr.model.*;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

// TODO add relations between components

public class LoanSystemContainerDiagram {
    public static void main(String[] args) throws StructurizrClientException {
        String api = args[0];
        String secret = args[1];
        long workspaceId = Long.parseLong(args[2]);

        ClientWrapper strClient = new ClientWrapper(api, secret, workspaceId);

        Workspace workspace = new Workspace(Constants.WORKSPACE_NAME, Constants.WORKSPACE_DESCR);
        Model model = workspace.getModel();
        ViewSet view = workspace.getViews();

        Person administrator = model.addPerson("Administrator");
        Person borrower = model.addPerson("Borrower");
        Person advisor = model.addPerson("Advisor");

        SoftwareSystem apiServer = model.addSoftwareSystem("API server", "System serving API, built as a modular monolith");
        SoftwareSystem spa = model.addSoftwareSystem("SPA", "Web app serving the functionalities to the useres");
        SoftwareSystem backoffice = model.addSoftwareSystem("Backoffice system", "App serving the functionalities for administrators");
        SoftwareSystem bik = model.addSoftwareSystem("BIK", "In polish: Biuro Obsługi Klienta");
        SoftwareSystem bankSystem = model.addSoftwareSystem("Bank's own system");

        administrator.uses(backoffice, "Uses");
        borrower.uses(spa, "Uses");
        advisor.uses(spa, "Uses");

        apiServer.uses(bik, "Uses");
        apiServer.uses(bankSystem, "Uses");
        spa.uses(apiServer, "Uses");
        backoffice.uses(apiServer, "Uses");

        Container monolith = apiServer.addContainer("Modular monolith");
        spa.uses(monolith, "Uses");
        backoffice.uses(monolith, "Uses");
        monolith.uses(bankSystem, "Uses");
        monolith.uses(bik, "Uses");

        Component templateCatalogue = monolith.addComponent("Template catalogue");
        Component ratings = monolith.addComponent("Ratings");
        Component draftsGenerator = monolith.addComponent("Drafts generator");
        Component advisorsDb = monolith.addComponent("Advisor database");
        Component draftsFiller = monolith.addComponent("Drafts filler");
        Component requestGenerator = monolith.addComponent("Request generator");
        Component requestSubmitter = monolith.addComponent("Request submitter");
        Component allowances = monolith.addComponent("Allowances");
        Component commissions = monolith.addComponent("Commissions");

        administrator.uses(templateCatalogue, "Creates offer in");
        administrator.uses(advisorsDb, "Manages");
        borrower.uses(ratings, "Uses");
        borrower.uses(draftsFiller, "Fills is");
        borrower.uses(requestGenerator, "Generates");
        advisor.uses(draftsGenerator, "Generates");
        advisor.uses(requestSubmitter, "Submits");

        ratings.uses(bik, "Asks for data");
        allowances.uses(bankSystem, "Asks for allowance");
        commissions.uses(bankSystem, "Gets calculated commission");

        SystemContextView systemContextView = view.createSystemContextView(apiServer, "API server", "System serving the business logic");
        systemContextView.addAllSoftwareSystems();
        systemContextView.addAllPeople();

        ContainerView modularMonolithView = view.createContainerView(apiServer, "Modular monolith being the API server", "Modular monolith serving the business logic");
        modularMonolithView.add(monolith);
        modularMonolithView.addAllSoftwareSystems();
        modularMonolithView.addAllPeople();

        ComponentView componentView = view.createComponentView(monolith, "Components diagram for monolith", "Components diagrams creating the modular monolith");
        componentView.add(bik);
        componentView.add(bankSystem);
        componentView.addAllContainers();
        componentView.addAllComponents();
        componentView.addAllPeople();

        strClient.putWorkspace(workspace);
    }
}
