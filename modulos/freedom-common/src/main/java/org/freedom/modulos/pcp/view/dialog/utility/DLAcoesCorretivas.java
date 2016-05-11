package org.freedom.modulos.pcp.view.dialog.utility;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.freedom.infra.model.jdbc.DbConnection;

import org.freedom.bmps.Icone;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.modulos.pcp.view.dialog.utility.DLAcaoCorretiva.EMs;

public class DLAcoesCorretivas extends FFDialogo implements MouseListener {

	private static final long serialVersionUID = 1L;

	private final JLabelPad lbImg = new JLabelPad( Icone.novo( "ishikawa.jpg" ) );

	private final JLabelPad lbM1 = new JLabelPad();

	private final JLabelPad lbM2 = new JLabelPad();

	private final JLabelPad lbM3 = new JLabelPad();

	private final JLabelPad lbM4 = new JLabelPad();

	private final JLabelPad lbM5 = new JLabelPad();

	private final JLabelPad lbM6 = new JLabelPad();

	private Object[] keys;

	public DLAcoesCorretivas( DbConnection con, Object[] keys ) {

		setTitulo( "Acões corretivas" );
		setAtribos( 670, 480 );
		setConexao( con );

		adic( lbM1, 26, 46, 116, 61 );
		adic( lbM2, 182, 46, 116, 61 );
		adic( lbM3, 341, 46, 116, 61 );
		adic( lbM4, 26, 225, 116, 61 );
		adic( lbM5, 182, 225, 116, 61 );
		adic( lbM6, 341, 225, 116, 61 );
		adic( lbImg, 10, 10, 635, 371 );

		lbM1.addMouseListener( this );
		lbM2.addMouseListener( this );
		lbM3.addMouseListener( this );
		lbM4.addMouseListener( this );
		lbM5.addMouseListener( this );
		lbM6.addMouseListener( this );

		this.keys = keys;
	}

	public void mouseClicked( MouseEvent e ) {

		EMs m = null;

		if ( e.getSource() == lbM1 ) {
			m = EMs.MATERIAIS;
		}
		else if ( e.getSource() == lbM2 ) {
			m = EMs.MAQUINA;
		}
		else if ( e.getSource() == lbM3 ) {
			m = EMs.METODO;
		}
		else if ( e.getSource() == lbM4 ) {
			m = EMs.MEIO_AMBIENTE;
		}
		else if ( e.getSource() == lbM5 ) {
			m = EMs.MAO_DE_OBRA;
		}
		else if ( e.getSource() == lbM6 ) {
			m = EMs.MEDIDA;
		}

		if ( m != null ) {
			DLAcaoCorretiva acaoCorretiva = new DLAcaoCorretiva( con, m, keys );
			acaoCorretiva.setVisible( true );
			if ( acaoCorretiva.OK ) {
				ok();
			}
		}
	}

	public void mouseEntered( MouseEvent e ) {

	}

	public void mouseExited( MouseEvent e ) {

	}

	public void mousePressed( MouseEvent e ) {

	}

	public void mouseReleased( MouseEvent e ) {

	}
}
