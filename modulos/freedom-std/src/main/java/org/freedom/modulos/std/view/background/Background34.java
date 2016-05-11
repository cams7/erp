package org.freedom.modulos.std.view.background;

import java.awt.event.MouseEvent;

import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.crm.agenda.FAgenda;
import org.freedom.modulos.fnc.view.frame.utility.FManutPag;
import org.freedom.modulos.fnc.view.frame.utility.FManutRec;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.view.frame.crud.detail.FOrcamento;
import org.freedom.modulos.std.view.frame.crud.detail.FVenda;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;
import org.freedom.plugin.AbstractBackground;

public class Background34 extends AbstractBackground {

	private static final long serialVersionUID = 1L;

	public Background34() {

		super();

	}

	public void mouseClicked( MouseEvent e ) {

		if ( e.getSource() == lbAgenda ) {

			FAgenda tela = new FAgenda();

			if ( !Aplicativo.telaPrincipal.temTela( "Agenda" ) ) {

				Aplicativo.telaPrincipal.criatela( "Agenda", tela, con );
			}
		}
		else if ( e.getSource() == lbVenda ) {

			FVenda tela = new FVenda();

			if ( !Aplicativo.telaPrincipal.temTela( "Venda" ) ) {

				Aplicativo.telaPrincipal.criatela( "Venda", tela, con );
			}
		}
		else if ( e.getSource() == lbReceber ) {

			FManutRec tela = new FManutRec();

			if ( !Aplicativo.telaPrincipal.temTela( "Manutenção de contas a receber" ) ) {

				Aplicativo.telaPrincipal.criatela( "Manutenção de contas a receber", tela, con );
			}
		}
		else if ( e.getSource() == lbPagar ) {

			FManutPag tela = new FManutPag();

			if ( !Aplicativo.telaPrincipal.temTela( "Manutenção de contas a pagar" ) ) {

				Aplicativo.telaPrincipal.criatela( "Manutenção de contas a pagar", tela, con );
			}
		}
		else if ( e.getSource() == lbProduto ) {

			FProduto tela = new FProduto();

			if ( !Aplicativo.telaPrincipal.temTela( "Produtos" ) ) {

				Aplicativo.telaPrincipal.criatela( "Produtos", tela, con );
			}
		}
		else if ( e.getSource() == lbOrcamento ) {

			FOrcamento tela = new FOrcamento();

			if ( !Aplicativo.telaPrincipal.temTela( "Orçamento" ) ) {

				Aplicativo.telaPrincipal.criatela( "Orçamento", tela, con );
			}
		}
		else if ( e.getSource() == lbCliente ) {

			FCliente tela = new FCliente();

			if ( !Aplicativo.telaPrincipal.temTela( "Clientes" ) ) {

				Aplicativo.telaPrincipal.criatela( "Clientes", tela, con );
			}
		}
	}

}
