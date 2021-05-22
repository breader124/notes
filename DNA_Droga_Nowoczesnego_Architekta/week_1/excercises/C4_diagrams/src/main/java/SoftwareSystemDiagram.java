import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.api.StructurizrClientException;
import com.structurizr.model.*;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

public class SoftwareSystemDiagram {

    public static void main(String[] args) throws StructurizrClientException {
        if (args.length < 2) {
            return;
        }

        String api = args[0];
        String secret = args[1];
        long workspaceId = Long.parseLong(args[2]);

        Workspace w = new Workspace("Banking system workspace", "Workspace containing diagrams of banking platform");
        Model model = w.getModel();
        ViewSet viewSet = w.getViews();

        Person customer = model.addPerson("User", "Customer using mobile application");

        SoftwareSystem apiSystem = model.addSoftwareSystem("API system", "System exposing API to be later used by mobile systems");
        SoftwareSystem androidAppSystem = model.addSoftwareSystem("Android app", "Android app consuming exposed API");
        SoftwareSystem iosAppSystem = model.addSoftwareSystem("iOS app", "iOS app consuming exposed API");
        SoftwareSystem mainframeSystem = model.addSoftwareSystem("MF system", "System containing business logic");

        customer.uses(androidAppSystem, "Uses");
        customer.uses(iosAppSystem, "Uses");
        androidAppSystem.uses(apiSystem, "Uses", "REST");
        iosAppSystem.uses(apiSystem, "Uses", "REST");
        apiSystem.uses(mainframeSystem, "Uses", "Internal communication method / Messaging");

        Container servletContainer = apiSystem.addContainer(
                "Servlets",
                "Contains servlets exposing API over web using REST",
                "JEE Servlet");
        Container ejbContainer = apiSystem.addContainer(
                "EJBs",
                "Contains EJBs acting as facade to MF programs",
                "JEE EJB"
        );
        Container sharedEjbsContainer = apiSystem.addContainer(
                "EJBs shared with other systems",
                "Contains EJBs shared with other systems, they act as a facade to MF programs",
                "JEE EJB"
        );
        androidAppSystem.uses(servletContainer, "Calls", "REST");
        iosAppSystem.uses(servletContainer, "Calls", "REST");
        servletContainer.uses(ejbContainer, "Calls", "Function invocation");
        servletContainer.uses(sharedEjbsContainer, "Calls", "Function invocation");
        ejbContainer.uses(mainframeSystem, "Calls", "Internal communication strategy");
        sharedEjbsContainer.uses(mainframeSystem, "Calls", "Internal communication strategy");

        Component touchIdComponent = servletContainer.addComponent(
                "TouchID enrollment",
                "Component giving the end user possibility to enroll for login with TouchID in iOS mobile application");
        Component openBankingComponent = servletContainer.addComponent(
                "PSD2 TPP",
                "Component providing integration with PSD2 TPP"
        );
        touchIdComponent.uses(ejbContainer, "Calls", "Function invocation");
        openBankingComponent.uses(sharedEjbsContainer, "Calls", "Function invocation");
        iosAppSystem.uses(touchIdComponent, "Calls", "REST");
        iosAppSystem.uses(openBankingComponent, "Calls", "REST");
        androidAppSystem.uses(openBankingComponent, "Calls", "REST");
        model.addImplicitRelationships();

        SystemContextView systemContextView = viewSet.createSystemContextView(
                apiSystem,
                "Mobile banking solution diagram",
                "Systems forming mobile banking solution and their relations between each other"
        );
        systemContextView.addNearestNeighbours(apiSystem);
        systemContextView.add(customer);
        systemContextView.addAnimation(apiSystem);
        systemContextView.addAnimation(mainframeSystem);
        systemContextView.addAnimation(iosAppSystem);
        systemContextView.addAnimation(androidAppSystem);

        ContainerView apiSystemContainerView = viewSet.createContainerView(
                apiSystem,
                "API system content",
                "Containers forming API system"
        );
        apiSystemContainerView.add(iosAppSystem);
        apiSystemContainerView.add(androidAppSystem);
        apiSystemContainerView.add(mainframeSystem);
        apiSystemContainerView.addAllContainers();

        apiSystemContainerView.addAnimation(servletContainer);
        apiSystemContainerView.addAnimation(iosAppSystem, androidAppSystem);
        apiSystemContainerView.addAnimation(ejbContainer, sharedEjbsContainer);
        apiSystemContainerView.addAnimation(mainframeSystem);

        ComponentView servletContainerComponentView = viewSet.createComponentView(
                servletContainer,
                "Servlet container context",
                "Components forming servlet container");
        servletContainerComponentView.add(touchIdComponent);
        servletContainerComponentView.add(openBankingComponent);
        servletContainerComponentView.add(ejbContainer);
        servletContainerComponentView.add(sharedEjbsContainer);
        servletContainerComponentView.add(iosAppSystem);
        servletContainerComponentView.add(androidAppSystem);

        StructurizrClient client = new StructurizrClient(api, secret);
        client.putWorkspace(workspaceId, w);
    }
}
