/**
 *  ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
/*
 * OrderView.java
 *
 * Created on August 4, 2006, 6:58 PM
 */

package com.floreantpos.ui.views.order.modifier;

import java.awt.BorderLayout;
import java.awt.Color;      //???
import java.awt.Component;      //???
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;        //???

import javax.swing.BoxLayout;   //???
import javax.swing.border.CompoundBorder;   //???
import javax.swing.border.EmptyBorder;  //???
import javax.swing.JLabel;       //???
import javax.swing.JPanel;
import javax.swing.JScrollPane;     //???
import javax.swing.border.TitledBorder; //???
import javax.swing.UIManager;   //???

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.ModifierGroup;
import com.floreantpos.model.Multiplier;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import net.miginfocom.swing.MigLayout;

import com.floreantpos.misc.WrapLayout;

//???   start
import com.floreantpos.ui.dialog.POSMessageDialog; 
import com.floreantpos.ui.views.order.OrderView;  
import com.floreantpos.ui.views.order.SelectionView;
import com.floreantpos.model.dao.MultiplierDAO;
import javax.swing.ButtonGroup;
import com.floreantpos.swing.POSToggleButtonWL;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import com.floreantpos.ui.views.order.SelectionViewWL;
import javax.swing.BorderFactory;

//???   end
/**
 *
 * @author  MShahriar
 */
public class ModifierSelectionDialog extends POSDialog implements ModifierGroupSelectionListener, ModifierSelectionListener {

//	private Vector<ModifierSelectionListener> listenerList = new Vector<ModifierSelectionListener>();


	private ModifierSelectionModel modifierSelectionModel;

	private ModifierGroupView modifierGroupView;
	private ModifierView modifierView;
	private TicketItemModifierTableView ticketItemModifierView;

	private JPanel westPanel = new JPanel(new BorderLayout(5, 5));

	private com.floreantpos.swing.TransparentPanel buttonPanel;

	private com.floreantpos.swing.PosButton btnSave;
	private com.floreantpos.swing.PosButton btnCancel;

//???   start
	private static JPanel pnlNorth;       
	private JPanel pnlCenter;      
	private JPanel pnlModifiers;   
	private JPanel pnlGrid;
	private Dimension screenSize;
	private Dimension viewSize;
	private Integer viewWidth;
	private JScrollPane scrlPane;
	private String activeModifierView = "";
    private JPanel multiplierPanel;
	private Multiplier selectedMultiplier;
	private MultiplierButton defaultMultiplierButton;
	protected JPanel actionButtonPanel = new JPanel(new MigLayout("fill,hidemode 3, ins 2", "sg, fill", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	private PosButton btnClear = new PosButton(POSConstants.CLEAR);
	private PosButton btnDone = new PosButton(POSConstants.GROUP.toUpperCase() + " " + "DONE");
	
	
//???   end


	public ModifierSelectionDialog(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;
		
        this.setName ("ModifierSelectionDialog");

		initComponents();
	}

//???   start
    public String getActiveModifierView ()
    {
        return activeModifierView;
    }
    
    public void setActiveModifierView (String view)
    {
        activeModifierView = view;
    }
//???  end
		
	
	private void initComponents() {
		setTitle("MODIFIERS");

		setLayout(new java.awt.BorderLayout(10, 10));  
		screenSize = Application.getPosWindow().getSize();

		screenSize.width = screenSize.width - 32;
		setSize(screenSize);
		
		//???  create west/left side ticket panel
        ticketItemModifierView = new TicketItemModifierTableView(modifierSelectionModel, this);
		
		westPanel.add(ticketItemModifierView);
        
		buttonPanel = new com.floreantpos.swing.TransparentPanel();
		buttonPanel.setLayout(new MigLayout("fill, ins 1 4 8 4", "fill", ""));
		buttonPanel.setName ("buttonPanel");

		createButtonPanel();
		
		westPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
		
		add(westPanel, BorderLayout.WEST);
		ticketItemModifierView.addModifierSelectionListener(this);
		

		//???  create center panel
         pnlCenter = new JPanel (new BorderLayout ());
        pnlCenter.setName ("pnlCenter");
        TitledBorder border = new TitledBorder("MODIFIERS");
        border.setTitleJustification(TitledBorder.CENTER);
        pnlCenter.setBorder(new CompoundBorder(border, new EmptyBorder(2, 2, 2, 2)));

  		add (pnlCenter, BorderLayout.CENTER);   //???
        
   //???###     pnlCenter.add (pnlNorth, BorderLayout.NORTH);


        
		modifierGroupView = new com.floreantpos.ui.views.order.modifier.ModifierGroupView(modifierSelectionModel);
		modifierGroupView.setName ("modifierGroupView");
        modifierGroupView.addModifierGroupSelectionListener(this);
        
        pnlNorth = new JPanel ();
        pnlNorth.setLayout (new BoxLayout(pnlNorth, BoxLayout.Y_AXIS));
        pnlNorth.setName ("pnlNorth");
        pnlNorth.add (modifierGroupView);
       
        pnlModifiers = new JPanel ();
        pnlModifiers.setLayout (new BoxLayout(pnlModifiers, BoxLayout.Y_AXIS));
        pnlModifiers.setName ("pnlModifiers");
        
        pnlCenter.add (pnlModifiers, BorderLayout.CENTER);

        
        pnlGrid = new JPanel ();
        pnlGrid.setLayout (new WrapLayout());
        pnlGrid.setName ("pnlGrid");

        scrlPane = new JScrollPane(pnlGrid,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);        
        
		pnlModifiers.add (scrlPane);

        
        //???		modifierView = new ModifierView (modifierSelectionModel);
        //???		add(modifierView);  center
        //??? modifierView.addModifierSelectionListener (this);
        //***		modifierGroupView.selectFirst();
        
        
        addModifiers ();        //???
 
        // now add the buttons to the pnlcenter.sourh border
        //  use modifierView.getName()
        addMultiplierButtons(); //???
        addActionButtons();     //???
        
        //actionButtonPanel.add (multiplierPanel, "span");     //???
        
        pnlCenter.add (actionButtonPanel, BorderLayout.SOUTH);
      
        revalidate();
        repaint ();

        checkForAllModifiers ();  
        
 	}
	

	public void addModifiers ()
	{
	
        Component panel = pnlNorth.getComponent (0);
        ModifierGroupView modifierGroups = (ModifierGroupView) panel;
        int j = modifierGroups.getComponentCount ();
 
        for (int i = 0; i < j; i++)
        {
        // spin teh modifier group view 
            activeModifierView = i + "";;
            
            modifierView = new ModifierView(modifierSelectionModel, this, activeModifierView);
            // pnlCenter);

            modifierView.setName ("modifierview: " + i);  
            
            modifierView.addModifierSelectionListener(this);
            
            ModifierGroupView.ModifierGroupButton  modifierGroup = (ModifierGroupView.ModifierGroupButton)  modifierGroups.getComponent(i);
            
            //???### do a fire metho in the action performed??
            modifierGroups.fireModifierGroupSelected(modifierGroup.menuModifierGroup);
            
            pnlGrid.add (modifierView);
        }
        /** spin the last created modifierView 
        j = modifierView.getComponentCount ();
        for (int i = 0; i < j; i++)
        {
            if (modifierView.getComponent (i).getName() == "actionbuttons")
            {
                pnlCenter.add (modifierView.getComponent (i), BorderLayout.SOUTH);
            }
        }
        ****/
        checkForAllModifiers ();  

	}
	
	public void createButtonPanel() {
		Dimension preferredButtonSize = new Dimension(100, 53);

		btnSave = new PosButton("DONE");
		btnSave.setName ("btrnSave");
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinishModifierSelection();
			}
		});
		btnSave.setPreferredSize(preferredButtonSize);

		btnCancel = new PosButton(POSConstants.CANCEL.toUpperCase());
		btnCancel.setName ("btnCancel");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setCanceled(true);
				dispose();
			}
		});
        
        btnCancel.setPreferredSize(preferredButtonSize);

		buttonPanel.add(btnCancel);
		buttonPanel.add(btnSave);

	}

	public com.floreantpos.ui.views.order.modifier.ModifierGroupView getModifierGroupView() {
		return modifierGroupView;
	}

	public void setModifierGroupView(com.floreantpos.ui.views.order.modifier.ModifierGroupView modifierGroupView) {
		this.modifierGroupView = modifierGroupView;
	}

	public ModifierView getModifierView() {
		return modifierView;
	}

	public void setModifierView (ModifierView modifierView) {
		this.modifierView = modifierView;
	}

	//???  start
	public void setModifierGroupView (String  view) 
	{
        this.modifierView = (ModifierView)  pnlGrid.getComponent (Integer.parseInt(view));
	}
	//???  end
	
	private void doFinishModifierSelection() {
		List<MenuItemModifierGroup> menuItemModiferGroups = modifierSelectionModel.getMenuItem().getMenuItemModiferGroups();
		if (menuItemModiferGroups == null) {
			dispose();
			return;
		}

		for (MenuItemModifierGroup menuItemModifierGroup : menuItemModiferGroups) {
			if (!isRequiredModifiersAdded (modifierSelectionModel.getTicketItem(), menuItemModifierGroup)) {
				showModifierSelectionMessage(menuItemModifierGroup);
				//??? modifierGroupView.setSelectedModifierGroup (menuItemModifierGroup.getModifierGroup());
				return;
			}
		}

		setCanceled(false);
		dispose();
	}

	@Override
	public void modifierGroupSelected(ModifierGroup menuModifierGroup) {
		modifierView.setModifierGroup (menuModifierGroup);
	}

	@Override
	public void modifierSelected(MenuModifier modifier, Multiplier multiplier) {
		//		if (addOnMode) {
		//			modifierSelectionModel.getTicketItem().addAddOn(modifier);
		//			updateView();
		//			return;
		//		}

		TicketItem ticketItem = modifierSelectionModel.getTicketItem();
		MenuItemModifierGroup menuItemModifierGroup = modifier.getMenuItemModifierGroup();

		int numModifiers = ticketItem.countModifierFromGroup(menuItemModifierGroup);
		int minQuantity = menuItemModifierGroup.getMinQuantity();
		int maxQuantity = menuItemModifierGroup.getMaxQuantity();

		if (maxQuantity < minQuantity) {
			maxQuantity = minQuantity;
		}

		if (numModifiers >= maxQuantity) {
			POSMessageDialog.showError("You have added maximum number of allowed modifiers from group " + modifier.getModifierGroup().getDisplayName());
			//			return;
			//			if (Application.getInstance().getRestaurant().isAllowModifierMaxExceed()) {
			//				ticketItem.addAddOn(modifier);
			//			}
			//			updateView();
			return;
		}

		TicketItemModifier ticketItemModifier = ticketItem.findTicketItemModifierFor(modifier, multiplier);
		if (ticketItemModifier == null) {
			OrderType type = ticketItem.getTicket().getOrderType();
			ticketItem.addTicketItemModifier(modifier, TicketItemModifier.NORMAL_MODIFIER, type, multiplier);
		}
		else {
			ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() + 1);
		}
		updateView();
		if ((numModifiers + 1) == maxQuantity) {
            modifierGroupSelectionDone(modifier.getModifierGroup());
		}
	}

	private void updateView() {
        modifierSelectionModel.getTicketItem().calculatePrice();
		modifierView.updateView();
		ticketItemModifierView.updateView();
	}
	
//???   start
	private void updateViews() 
	{
        modifierSelectionModel.getTicketItem().calculatePrice();
		// spin the modifierlist clear modifiers
		int j = pnlGrid.getComponentCount();
		for (int i = 0; i < j; i++)
		{
            modifierView = (ModifierView) pnlGrid.getComponent(i);
            modifierView.updateView();
        }
		// end spin
		ticketItemModifierView.updateView();
	}
//???   end

	@Override
	public void clearModifiers(ModifierGroup modifierGroup) {
		TicketItem ticketItem = modifierSelectionModel.getTicketItem();
		List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
		if (ticketItemModifiers != null) {
			for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
				TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
				if (!ticketItemModifier.isPrintedToKitchen()) {
					iterator.remove();
				}
			}
		}

		List<TicketItemModifier> addOnsList = ticketItem.getAddOns();
		if (addOnsList != null) {
			for (Iterator iterator = addOnsList.iterator(); iterator.hasNext();) {
				TicketItemModifier addOns = (TicketItemModifier) iterator.next();
				if (!addOns.isPrintedToKitchen()) {
					iterator.remove();
				}
			}
		}

		updateView();
	}

//???   start
	public void clearGroupsModifiers() 
	{
		TicketItem ticketItem = modifierSelectionModel.getTicketItem();
		List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
		if (ticketItemModifiers != null) {
			for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
				TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
				if (!ticketItemModifier.isPrintedToKitchen()) {
					iterator.remove();
				}
			}
		}

		List<TicketItemModifier> addOnsList = ticketItem.getAddOns();
		if (addOnsList != null) {
			for (Iterator iterator = addOnsList.iterator(); iterator.hasNext();) {
				TicketItemModifier addOns = (TicketItemModifier) iterator.next();
				if (!addOns.isPrintedToKitchen()) {
					iterator.remove();
				}
			}
		}

		updateViews();
	}
//???   end	
	
	
	@Override
	public void modifierGroupSelectionDone(ModifierGroup modifierGroup) {
		MenuItemModifierGroup menuItemModifierGroup = modifierGroup.getMenuItemModifierGroup();
		if (!isRequiredModifiersAdded (modifierSelectionModel.getTicketItem(), menuItemModifierGroup)) {
			showModifierSelectionMessage(menuItemModifierGroup);
		//???	modifierGroupView.setSelectedModifierGroup (menuItemModifierGroup.getModifierGroup());
			return;
		}

		if (modifierGroupView.hasNextMandatoryGroup()) {
			modifierGroupView.selectNextGroup();
		}
	}

	public ModifierSelectionModel getModifierSelectionModel() {
		return modifierSelectionModel;
	}

	public void setModifierSelectionModel(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;
	}

	public static boolean isRequiredModifiersAdded (TicketItem ticketItem, MenuItemModifierGroup menuItemModifierGroup) 
	{
		return ticketItem.requiredModifiersAdded(menuItemModifierGroup);
	}

	private void showModifierSelectionMessage(MenuItemModifierGroup menuItemModifierGroup) {
		String displayName = menuItemModifierGroup.getModifierGroup().getDisplayName();
		int minQuantity = menuItemModifierGroup.getMinQuantity();
		POSMessageDialog.showError("You must select at least " + minQuantity + " modifiers from group " + displayName);
	}

	@Override
	public void modifierRemoved(TicketItemModifier modifier) {
		updateView();
	}

	//???***
	public void finishModifierSelection() {
        finishModifierSelection(true);
	}
	

	public void finishModifierSelection(boolean showMessage) {
		TicketItem ticketItem = modifierSelectionModel.getTicketItem();
		List<MenuItemModifierGroup> menuItemModiferGroups = modifierSelectionModel.getMenuItem().getMenuItemModiferGroups();
		if (menuItemModiferGroups == null) {
			setCanceled(false);
			dispose();
			return;
		}
		if (!menuItemModiferGroups.isEmpty()) {

			for (Iterator iterator = menuItemModiferGroups.iterator(); iterator.hasNext();) {
				MenuItemModifierGroup ticketItemModifierGroup = (MenuItemModifierGroup) iterator.next();
				boolean hasModifiers = !ticketItemModifierGroup.getModifierGroup().getModifiers().isEmpty();
				if (!ticketItem.requiredModifiersAdded(ticketItemModifierGroup) && hasModifiers) {
				  if (showMessage) {
				   //??? modifierGroupSelected(ticketItemModifierGroup.getModifierGroup());
					
                    POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Please select minimum quantity of each group!");
					return;
                  }
				}
				//				if (!ticketItemModifiers.isEmpty()) {
				//					int size = ticketItemModifiers.size();
				//					if (size < minQuantity) {
				//						POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Please select minimum quantity of each group!");
				//						return;
				//					}
				//					else if (size > maxQuantity) {
				//						POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Please select quantity below the maximum quantity of each group!");
				//						return;
				//					}
				//					else {
				//						setCanceled(false);
				//						dispose();
				//					}
				//				}
			}
		}
		setCanceled(false);
		dispose();
	}

//???
//???   copied from ModifierView.java
//???   modified to work from the dialog

//???   start

	private void addMultiplierButtons() {
		multiplierPanel = new JPanel(new MigLayout("ins 0,fillx,center"));
		multiplierPanel.setName ("multiplier");
		
		List<Multiplier> multiplierList = MultiplierDAO.getInstance().findAll();
		ButtonGroup group = new ButtonGroup();
		if (multiplierList != null) {
            for (Multiplier multiplier : multiplierList) {
            //???***  what here for narrower buttons
				MultiplierButton btnMultiplier = new MultiplierButton(multiplier);
				if (multiplier.isDefaultMultiplier()) {
					selectedMultiplier = multiplier;
					defaultMultiplierButton = btnMultiplier;
					btnMultiplier.setSelected(true);
				}
				multiplierPanel.add(btnMultiplier, "grow");
				group.add(btnMultiplier);
			}
		}
        actionButtonPanel.add(multiplierPanel, "span");
	}


	private void addActionButtons() {
		actionButtonPanel.add(btnClear);
		actionButtonPanel.add(btnDone);

		btnDone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
             //   for (ModifierSelectionListener listener : ModifierSelectionDialog.this.listenerList) {
					// listener.
					finishModifierSelection();//modifierGroupSelectionDone(modifierGroup);
			//	}
			}
		});
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
			//	for (ModifierSelectionListener listener :  this.listenerList) {
                    // modifierView.
              //      istener.
              clearGroupsModifiers();
			//	}
			}
		});
	}


	private class MultiplierButton extends POSToggleButtonWL implements ActionListener {
		private Multiplier multiplier;

		public MultiplierButton(Multiplier multiplier) {
			this.multiplier = multiplier;
			setText(multiplier.getName());
			Integer buttonColor = multiplier.getButtonColor();
			if (buttonColor != null) {
				setBackground(new Color(buttonColor));
			}
			Integer textColor = multiplier.getTextColor();
			if (textColor != null) {
				setForeground(new Color(textColor));
			}
			setBorder(BorderFactory.createLineBorder(Color.GRAY));
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			selectedMultiplier = multiplier;
            int j = pnlGrid.getComponentCount ();
            for (int i =0; i < j; i++)
            {
                ModifierView modifier = (ModifierView)  pnlGrid.getComponent (i);  modifier.setMultiplier (multiplier);
            }
        }

		public Multiplier getMultiplier() {
			return multiplier;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (isSelected())
				setBorder(BorderFactory.createLineBorder(new Color(255, 128, 0), 1));
			else
                setBorder(BorderFactory.createLineBorder(Color.GRAY));

		}
	}	
	
//???   end copy

	
//???  start
    public void resetMultipliers()
    {
        int j = pnlGrid.getComponentCount ();
        for (int i = 0; i < j; i++)
        {
            ModifierView modifier = (ModifierView) pnlGrid .getComponent (i);
            modifier.resetMultiplier ();
        }
        defaultMultiplierButton.setSelected(true);
        selectedMultiplier = defaultMultiplierButton.getMultiplier();        
    } 

    public boolean checkForAllModifiers () 
    {
        TicketItem ticketItem = modifierSelectionModel.getTicketItem();
        List<MenuItemModifierGroup> menuItemModiferGroups = modifierSelectionModel.getMenuItem().getMenuItemModiferGroups();
		
        boolean haveAll = true;
        if (!menuItemModiferGroups.isEmpty()) 
        {
            for (Iterator iterator = menuItemModiferGroups.iterator(); iterator.hasNext();) 
            {
                MenuItemModifierGroup ticketItemModifierGroup = (MenuItemModifierGroup) iterator.next();
                boolean hasModifiers = !ticketItemModifierGroup.getModifierGroup().getModifiers().isEmpty();
                if (!ticketItem.requiredModifiersAdded (ticketItemModifierGroup) && hasModifiers) 
                {
                    haveAll = false;
                }
            }
        }
        
        // spin the pnlGrid.getComponent (modigfierView)

        if (haveAll == false)
        {
            // modifierView.  //???
            btnDone.setBackground (UIManager.getColor("Control"));
        }
        else
        {
            // modifierView.    //???
            btnDone.setBackground (Color.green);
        }
        return haveAll;
    }
//???  end
	
	
}
