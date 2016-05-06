package org.freedom.plugin;

public abstract class AbstractControleVendaPDV extends AbstractControle {

	public AbstractControleVendaPDV() {

		super();
	}

	public abstract boolean beforeAbreVenda();

	public abstract boolean afterAbreVenda();

	public abstract boolean beforeVendaItem();

	public abstract boolean afterVendaItem();

	public abstract boolean beforeCancelaItem();

	public abstract boolean afterCancelaItem();

	public abstract boolean beforeCancelaVenda();

	public abstract boolean afterCancelaVenda();

	public abstract boolean beforeFechaVenda();

	public abstract boolean afterFechaVenda();

}
