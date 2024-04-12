package org.cftoolsuite.cfapp.ui;

import org.cftoolsuite.cfapp.ui.view.HomeView;
import org.cftoolsuite.cfapp.ui.view.SnapshotApplicationDetailView;
import org.cftoolsuite.cfapp.ui.view.SnapshotServiceInstanceDetailView;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;


public class MainLayout extends AppLayout {

    private static final long serialVersionUID = 1L;

    public MainLayout() {
    	Tab homeTab = createTab(VaadinIcon.HOME.create(), "Home", HomeView.class);

    	Accordion accordion = new Accordion();

    	Tabs snapshotDetailTabs = createTabs();
    	Tab sadTab = createTab(VaadinIcon.TABLE.create(), "Application", SnapshotApplicationDetailView.class);
    	Tab sidTab = createTab(VaadinIcon.TABLE.create(), "Service Instance", SnapshotServiceInstanceDetailView.class);

    	snapshotDetailTabs.add(sadTab, sidTab);
    	accordion.add("Snapshot Detail", snapshotDetailTabs).addThemeVariants(DetailsVariant.REVERSE);

    	addToNavbar(true, homeTab, new DrawerToggle());
    	addToDrawer(accordion);
    }

    private Tabs createTabs() {
    	Tabs menu = new Tabs();
    	menu.setWidthFull();
    	menu.setOrientation(Tabs.Orientation.VERTICAL);
    	menu.setFlexGrowForEnclosedTabs(1);
    	return menu;
    }

    private Tab createTab(Icon icon, String label, Class<? extends Component> layout) {
		RouterLink link = new RouterLink();
    	link.setRoute(layout);
		Div container = new Div();
		container.getStyle().set("display", "flex");
		container.getStyle().set("justify-content", "flex-start");
		container.getStyle().set("align-items", "center");
		container.getStyle().set("width", "100%"); // Ensure it takes up the full width
		icon.getStyle().set("margin-right", "8px"); // Adjust the space as needed
		container.add(icon, new Span(label));
		link.add(container);
		Tab tab = new Tab();
		tab.add(link);
		return tab;
    }

}