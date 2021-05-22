import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.api.StructurizrClientException;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
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

        SystemContextView systemContextView = viewSet.createSystemContextView(
                apiSystem,
                "Mobile banking solution diagram",
                "Systems forming mobile banking solution and their relations between each other"
        );
        systemContextView.addNearestNeighbours(apiSystem);
        systemContextView.addAnimation(apiSystem);
        systemContextView.addAnimation(mainframeSystem);
        systemContextView.addAnimation(iosAppSystem);
        systemContextView.addAnimation(androidAppSystem);

        StructurizrClient client = new StructurizrClient(api, secret);
        client.putWorkspace(workspaceId, w);
    }
}
