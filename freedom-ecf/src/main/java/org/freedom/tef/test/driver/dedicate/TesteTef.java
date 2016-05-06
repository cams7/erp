
package org.freedom.tef.test.driver.dedicate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.freedom.tef.app.ControllerTef;
import org.freedom.tef.app.ControllerTefEvent;
import org.freedom.tef.app.ControllerTefListener;
import org.freedom.tef.driver.dedicate.DedicatedAction;
import org.freedom.tef.driver.dedicate.TypeFields;

public class TesteTef extends JFrame implements ControllerTefListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel cabecalho = null;

	private JLabel operador = null;

	private JLabel cliente = null;

	private JButton action = null;

	private JButton ler_cartao = null;

	private JButton verifica_pinpad = null;

	private JButton confirma_pinpad = null;

	private JButton continuar = null;

	// private DedicatedTef tef;
	private ControllerTef tef;

	private List<String> bufferTef = new ArrayList<String>();

	public TesteTef() throws Exception {

		super();
		// System.setProperty( ManagerIni.FILE_INIT_DEFAULT, "totem.ini" );
		// tef = DedicatedTef.getInstance( this );
		tef = ControllerTef.getControllerDedicatedTef( "totem.ini", this );
		initialize();
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		setLocationRelativeTo( this );
		continuar.setVisible( false );
	}

	private void initialize() {

		this.setSize( 500, 350 );
		this.setContentPane( getJContentPane() );
		this.setTitle( "JFrame" );
	}

	private JPanel getJContentPane() {

		if ( jContentPane == null ) {
			cliente = new JLabel();
			cliente.setBackground( Color.darkGray );
			cliente.setForeground( Color.white );
			cliente.setFont( new Font( "Verdana", Font.BOLD, 18 ) );
			cliente.setHorizontalAlignment( SwingConstants.CENTER );
			cliente.setOpaque( true );
			cliente.setBounds( new Rectangle( 10, 120, 470, 60 ) );
			operador = new JLabel();
			operador.setBackground( Color.darkGray );
			operador.setForeground( Color.white );
			operador.setFont( new Font( "Verdana", Font.BOLD, 18 ) );
			operador.setHorizontalAlignment( SwingConstants.CENTER );
			operador.setOpaque( true );
			operador.setBounds( new Rectangle( 10, 50, 470, 60 ) );
			cabecalho = new JLabel();
			cabecalho.setBackground( Color.darkGray );
			cabecalho.setForeground( Color.white );
			cabecalho.setHorizontalAlignment( SwingConstants.CENTER );
			cabecalho.setOpaque( true );
			cabecalho.setBounds( new Rectangle( 10, 10, 470, 30 ) );
			jContentPane = new JPanel();
			jContentPane.setLayout( null );
			jContentPane.add( cabecalho, null );
			jContentPane.add( operador, null );
			jContentPane.add( cliente, null );
			jContentPane.add( getAction(), null );
			jContentPane.add( getLer_cartao(), null );
			jContentPane.add( getVerifica_pinpad(), null );
			jContentPane.add( getConfirma_pinpad(), null );
			jContentPane.add( getContinuar(), null );
		}
		return jContentPane;
	}

	private JButton getAction() {

		if ( action == null ) {
			action = new JButton( "requisição de venda" );
			action.setBounds( new Rectangle( 310, 190, 170, 30 ) );
			action.addActionListener( this );
		}
		return action;
	}

	private JButton getVerifica_pinpad() {

		if ( verifica_pinpad == null ) {
			verifica_pinpad = new JButton( "verifica pinpad" );
			verifica_pinpad.setBounds( new Rectangle( 10, 190, 170, 30 ) );
			verifica_pinpad.addActionListener( this );
		}
		return verifica_pinpad;
	}

	private JButton getLer_cartao() {

		if ( ler_cartao == null ) {
			ler_cartao = new JButton( "ler cartão" );
			ler_cartao.setBounds( new Rectangle( 10, 230, 170, 30 ) );
			ler_cartao.addActionListener( this );
		}
		return ler_cartao;
	}

	private JButton getConfirma_pinpad() {

		if ( confirma_pinpad == null ) {
			confirma_pinpad = new JButton( "confirmar" );
			confirma_pinpad.setBounds( new Rectangle( 10, 270, 170, 30 ) );
			confirma_pinpad.addActionListener( this );
		}
		return confirma_pinpad;
	}

	private JButton getContinuar() {

		if ( continuar == null ) {
			continuar = new JButton( "Clique para continuar." );
			continuar.setBounds( new Rectangle( 10, 190, 470, 110 ) );
			continuar.addActionListener( this );
		}
		return continuar;
	}

	public synchronized boolean actionTef( ControllerTefEvent e ) {

		boolean action = false;
		checking: {
			if ( e.getSource() == tef ) {
				action = true;
				if ( e.getAction() == DedicatedAction.ERRO ) {
					operador.setBackground( Color.RED );
					cliente.setBackground( Color.RED );
					operador.setText( e.getMessage() );
					cliente.setText( e.getMessage() );
					return false;
				}
				else if ( e.getAction() == DedicatedAction.WARNING ) {
					operador.setText( e.getMessage() );
					cliente.setText( e.getMessage() );
				}
				else if ( e.getAction() == DedicatedAction.ARMAZENAR ) {
					System.out.println( "Armazenando... " + tef.getTypeField() + " | " + e.getMessage() );
					if ( tef.getTypeField() == TypeFields.VOUCHER_TEF.code() ) {
						bufferTef.add( e.getMessage() );
						break checking;
					}
				}
				else if ( e.getAction() == DedicatedAction.CHECK_TICKET ) {
					action = bufferTef.size() > 0;
					break checking;
				}
				else if ( e.getAction() == DedicatedAction.PRINT_TICKET ) {
					break checking;
				}
				if ( e.getAction() == DedicatedAction.MENSAGEM_OPERADOR ) {
					operador.setText( e.getMessage() );
				}
				else if ( e.getAction() == DedicatedAction.MENSAGEM_CLIENTE ) {
					cliente.setText( e.getMessage() );
				}
				else if ( e.getAction() == DedicatedAction.MENSAGEM_TODOS ) {
					operador.setText( e.getMessage() );
					cliente.setText( e.getMessage() );
				}
				else if ( e.getAction() == DedicatedAction.CABECALHO_MENU ) {
					cabecalho.setText( e.getMessage() );
				}
				else if ( e.getAction() == DedicatedAction.REMOVER_MESAGEM_OPERADOR ) {
					operador.setText( "" );
				}
				else if ( e.getAction() == DedicatedAction.REMOVER_MESAGEM_CLIENTE ) {
					operador.setText( "" );
				}
				else if ( e.getAction() == DedicatedAction.REMOVER_MESAGEM_TODOS ) {
					operador.setText( "" );
					cliente.setText( "" );
				}
				else if ( e.getAction() == DedicatedAction.REMOVER_CABECALHO_MENU ) {
					cabecalho.setText( "" );
				}
				else if ( e.getAction() == DedicatedAction.CABECALHO ) {
					setTitle( e.getMessage() );
				}
				else if ( e.getAction() == DedicatedAction.REMOVER_CABECALHO ) {
					setTitle( "" );
				}
				else if ( e.getAction() == DedicatedAction.RETORNAR_CONFIRMACAO ) {
					System.out.println( "RETORNAR CONFIRMAÇÃO !" );
				}
				else if ( e.getAction() == DedicatedAction.MOSTRAR_MENU ) {
					System.out.println( "MOSTRAR MENU !" );
				}
				else if ( e.getAction() == DedicatedAction.AGUADAR_TECLA_OPERADOR ) {
					operador.setText( "" );
					cliente.setText( "" );
					esperar( true );
					return false;
				}
				else {
					System.out.println( "\nComando " + e.getAction().code() + " " + e.getMessage() );
					// action = e.getAction().code()==23;
				}
			}
		}
		return action;
	}

	private void esperar( boolean arg ) {

		continuar.setVisible( arg );
		action.setVisible( !arg );
		ler_cartao.setVisible( !arg );
		verifica_pinpad.setVisible( !arg );
		confirma_pinpad.setVisible( !arg );
	}

	public void actionPerformed( final ActionEvent e ) {

		cabecalho.setText( "" );
		operador.setText( "" );
		cliente.setText( "" );
		operador.setBackground( Color.BLACK );
		cliente.setBackground( Color.BLACK );
		Thread th = new Thread( new Runnable() {

			public void run() {

				if ( e.getSource() == action ) {
					bufferTef.clear();
					tef.requestSale( 123456, new BigDecimal( "15.748" ), "Teste" );
				}
				else if ( e.getSource() == verifica_pinpad ) {
					if ( tef.checkPinPad() ) {
						operador.setText( "PinPad OK!" );
					}
				}
				else if ( e.getSource() == ler_cartao ) {
					tef.readCard( "  Sesc  Parana  " + " passe o cartão " );
				}
				else if ( e.getSource() == confirma_pinpad ) {
					if ( tef.readYesNoCard( "  Sesc  Parana  " + "   Confirma ?   " ) ) {
						operador.setText( "Confirmado !" );
						cliente.setText( "Confirmado !" );
					}
					else {
						operador.setText( "NÃO Confirmado !" );
						cliente.setText( "NÃO Confirmado !" );
					}
				}
				else if ( e.getSource() == continuar ) {
					esperar( false );
					tef.actionNextCommand();
				}
			}
		} );
		th.start();
	}

	public static void main( String[] args ) {

		try {
			TesteTef teste = new TesteTef();
			teste.setVisible( true );
		}
		catch ( Exception e ) {
			e.printStackTrace();
			JOptionPane.showMessageDialog( null, e.getMessage(), "Erro TEF", JOptionPane.ERROR_MESSAGE );
			System.exit( 0 );
		}
	}
}
