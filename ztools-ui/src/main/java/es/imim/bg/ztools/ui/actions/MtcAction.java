package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;

import es.imim.bg.ztools.stats.multitestcorrection.MultipleTestCorrection;
import es.imim.bg.ztools.ui.commands.MtcCommand;
import es.imim.bg.ztools.ui.commands.Command.CommandException;
import es.imim.bg.ztools.ui.model.ITableModel;

public class MtcAction extends BaseAction {

	private static final long serialVersionUID = 991170566166881702L;

	protected MultipleTestCorrection mtc;
	
	public MtcAction(MultipleTestCorrection mtc) {
		super(mtc.getName());
		
		setDesc("Calculate " + mtc.getName() + " multiple test correction");
		
		this.mtc = mtc;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		ITableModel tableModel = getTableModel();
		
		MtcCommand cmd = 
			new MtcCommand(mtc, tableModel);
		
		try {
			cmd.execute(createProgressMonitor());
		} catch (CommandException ex) {
			ex.printStackTrace();
		}
	}

}
