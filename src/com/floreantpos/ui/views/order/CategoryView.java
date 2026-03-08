package com.floreantpos.ui.views.order;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.explorer.QuickMaintenanceExplorer;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.swing.POSToggleButtonWL;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.ui.views.order.actions.CategorySelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import org.apache.log4j.Logger;

public class CategoryView extends SelectionViewWL implements ActionListener {
  private Vector<CategorySelectionListener> listenerList = new Vector<>();
  
  private ButtonGroup categoryButtonGroup;
  
  private Map<String, CategoryButton> buttonMap = new HashMap<>();
  
  private MenuCategory selectedCategory;
  
  public static final String VIEW_NAME = "CATEGORY_VIEW";
  
  private int categorySize = 0;
  
  public CategoryView() {
    super(POSConstants.CATEGORIES, PosUIManager.getSize(100), PosUIManager.getSize(80));
    this.categoryButtonGroup = new ButtonGroup();
  }
  
  public void initialize() {
    reset();
    MenuCategoryDAO categoryDAO = new MenuCategoryDAO();
    List<MenuCategory> categories = categoryDAO.findAllEnable();
    boolean maintenanceMode = RootView.getInstance().isMaintenanceMode();
    if (categories.size() == 0 && !maintenanceMode)
      return; 
    Ticket currentTicket = OrderView.getInstance().getCurrentTicket();
    if (currentTicket != null) {
      OrderType orderType = currentTicket.getOrderType();
      MenuGroupDAO menuGroupDAO = MenuGroupDAO.getInstance();
      if (maintenanceMode) {
        categories.add(new MenuCategory(null, ""));
      } else {
        for (Iterator<MenuCategory> iterator = categories.iterator(); iterator.hasNext(); ) {
          MenuCategory menuCategory = iterator.next();
          if (menuCategory.getId() == null)
            continue; 
          List<MenuGroup> menuGroups = menuCategory.getMenuGroups();
          for (Iterator<MenuGroup> iterator2 = menuGroups.iterator(); iterator2.hasNext(); ) {
            MenuGroup menuGroup = iterator2.next();
            if (!menuGroupDAO.hasChildren(null, menuGroup, orderType))
              iterator2.remove(); 
          } 
          if (menuGroups == null || menuGroups.size() == 0)
            iterator.remove(); 
        } 
      } 
      setItems(categories);
      CategoryButton categoryButton = null;
      if (maintenanceMode && !categories.isEmpty() && this.selectedCategory != null) {
        categoryButton = this.buttonMap.get(String.valueOf(this.selectedCategory.getId()));
      } else {
        categoryButton = (CategoryButton)getFirstItemButton();
      } 
      if (categoryButton != null) {
        categoryButton.setSelected(true);
        fireCategorySelected(categoryButton.foodCategory);
      } else {
        fireCategorySelected((MenuCategory)null);
      } 
      if (!maintenanceMode && categories.size() < 1) {
        setVisible(false);
      } else {
        setVisible(true);
      } 
      this.categorySize = categories.size();
    } 
  }
  
  protected JToggleButton createItemButton(Object item) {
    MenuCategory menuCategory = (MenuCategory)item;
    CategoryButton button = new CategoryButton(this, menuCategory);
    this.categoryButtonGroup.add((AbstractButton)button);
    this.buttonMap.put(String.valueOf(menuCategory.getId()), button);
    return (JToggleButton)button;
  }
  
  public void updateView(MenuCategory menuCategory) {
    this.selectedCategory = menuCategory;
    initialize();
  }
  
  public void addCategorySelectionListener(CategorySelectionListener listener) {
    this.listenerList.add(listener);
  }
  
  public void removeCategorySelectionListener(CategorySelectionListener listener) {
    this.listenerList.remove(listener);
  }
  
  private void fireCategorySelected(MenuCategory foodCategory) {
    this.selectedCategory = foodCategory;
    for (CategorySelectionListener listener : this.listenerList)
      listener.categorySelected(foodCategory); 
  }
  
  public void setSelectedCategory(MenuCategory category) {
    CategoryButton button = this.buttonMap.get(String.valueOf(category.getId()));
    if (button != null)
      button.setSelected(true); 
  }
  
  private static class CategoryButton extends POSToggleButtonWL {
    MenuCategory foodCategory;
    
    CategoryButton(CategoryView view, MenuCategory menuCategory) {
      updateView(menuCategory);
      addActionListener(view);
    }
    
    public void updateView(MenuCategory menuCategory) {
      this.foodCategory = menuCategory;
      if (this.foodCategory.getId() == null) {
        setIcon(IconFactory.getIcon("/ui_icons/", "add+user.png"));
      } else {
        setText(menuCategory.getDisplayName());
      } 
      if (menuCategory.getButtonColor() != null)
        setBackground(menuCategory.getButtonColor()); 
      if (menuCategory.getTextColor() != null)
        setForeground(menuCategory.getTextColor()); 
    }
  }
  
  public void actionPerformed(ActionEvent e) {
    CategoryButton button = (CategoryButton)e.getSource();
    if (button.isSelected()) {
      if (OrderView.getInstance().isVisible() && RootView.getInstance().isMaintenanceMode()) {
        QuickMaintenanceExplorer.quickMaintain(button.foodCategory);
        if (button.foodCategory.getId() == null)
          return; 
      } 
      fireCategorySelected(button.foodCategory);
    } 
  }
  
  public void cleanup() {
    Collection<CategoryButton> buttons = this.buttonMap.values();
    for (CategoryButton button : buttons)
      button.removeActionListener(this); 
    this.buttonMap.clear();
  }
  
  public void componentResized(ComponentEvent e) {
    int totalItem = getFitableButtonCount();
    if (totalItem == this.buttonPanelContainer.getComponentCount())
      return; 
    renderItems();
    if (!isInitialized()) {
      CategoryButton categoryButton = (CategoryButton)getFirstItemButton();
      if (categoryButton != null) {
        categoryButton.setSelected(true);
        fireCategorySelected(categoryButton.foodCategory);
      } 
    } 
  }
  
  protected int getFitableButtonCount() {
    return this.categorySize;
  }
  
  private static Logger logger = Logger.getLogger(MenuItemView.class);
}
