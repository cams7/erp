package org.freedom.infra.x.swing;

import org.jdesktop.swingx.mapviewer.TileFactoryInfo;

public class GoogleTileFactoryInfo extends TileFactoryInfo {
	boolean sateliteView;

	public GoogleTileFactoryInfo(int minimumZoomLevel, int maximumZoomLevel, int totalMapZoom, int tileSize, boolean xr2l, boolean yt2b, boolean sateliteView) {
		super(minimumZoomLevel, maximumZoomLevel, totalMapZoom, tileSize, xr2l, yt2b, null, null, null, null);
		this.sateliteView = sateliteView;
	}

	@Override
	public String getTileUrl(int x, int y, int zoom) {
		if (!sateliteView) {
			return getMapUrl(x, y, zoom);
		}
		else {
			return getSatURL(x, y, zoom);
		}
	}

	protected String getMapUrl(int x, int y, int zoom) {

		// String url = "http://mt.google.com/mt?w=2.43" + "&x=" + x + "&y=" + y
		// + "&zoom=" + zoom; // URL inativa...
		// String url = "http://mt.google.com/staticmap?v=w2.95" + "&x=" + x +
		// "&y=" + y + "&zoom=" + zoom;
		// String url =
		// "http://us.maps1.yimg.com/us.tile.maps.yimg.com/ximg?v=4.2" + "&x=" +
		// x + "&y=" + y + "&z=" + zoom + "&r=1";
		// String url = "http://mt2.google.com/mt?n=404&v=w2.75" + "&x=" + x +
		// "&y=" + y + "&zoom=" + zoom;
		// String url =
		// "http://khm3.google.com/kh/v=46&x=4574&y=2697&z=13&s=Galileo" + "&x="
		// + x + "&y=" + y + "&zoom=" + zoom;

		// O uso deste recurso do google não é legal, usado apenas para testes
		// no recurso.

		String url = "http://mt1.google.com/vt/lyr...@110&hl=pl" + "&x=" + x + "&y=" + y + "&zoom=" + zoom;

		return url;
	}

	public String getSatURL(int x, int y, int zoom) {
		int ya = 1 << ( 17 - zoom );
		if (( y < 0 ) || ( ya - 1 < y )) {
			return "http://www.google.com/mapfiles/transparent.gif";
		}
		if (( x < 0 ) || ( ya - 1 < x )) {
			x = x % ya;
			if (x < 0) {
				x += ya;
			}
		}

		StringBuffer str = new StringBuffer();
		str.append('t');
		for (int i = 16; i >= zoom; i--) {
			ya = ya / 2;
			if (y < ya) {
				if (x < ya) {
					str.append('q');
				}
				else {
					str.append('r');
					x -= ya;
				}
			}
			else {
				if (x < ya) {
					str.append('t');
					y -= ya;
				}
				else {
					str.append('s');
					x -= ya;
					y -= ya;
				}
			}
		}
		return "http://kh.google.com/kh?v=1&t=" + str;
	}

}