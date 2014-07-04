package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.datos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.datos.numericos.ElementType_Numeric;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.datos.texto.ElementType_Texto;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Funcion que parsea la tabla de categorias para los atributos numericos y de Texto.
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Datos implements InterfaceChasquiparser{

public enum Tabla {ATRIBUTOS_NUMERICOS,ATRIBUTOS_TEXTO};

	private CompleteElementType atributonumericotexto;
	private LoadCollectionChasqui LCole;

	/**
	 * Constructor de la clase por parametros.
	 * @param name Nombre de el meta
	 * @param browseable si el meta es navegable.
	 * @param father padre del meta.
	 */
	public ElementType_Datos(String name, boolean browseable,
			CompleteElementType father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributonumericotexto=new CompleteElementType(name, father);
		
		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		
		atributonumericotexto.getShows().add(VistaOV);
	}

	/**
	 * Funcion de copia del paramero por referencia.
	 * @param atributonumericotexto nuevo parametro.
	 */
	public ElementType_Datos(CompleteElementType atributonumericotexto,LoadCollectionChasqui lcole) {
		this.atributonumericotexto=atributonumericotexto;
		LCole=lcole;
	}

	/**
	 * Funcion que procesa los hijos de texto de la categoria.
	 */
	private void prosessSonsTexto() {
		try {
			//Calcula el numero u los atributos a designar :)
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT nom_atrib,max(cantidad) AS repeticiones FROM (" +
					"SELECT distinct idov, nom_atrib, count(*)" +
					"AS Cantidad " +
					"FROM atributos_texto WHERE categoria='"+atributonumericotexto.getName()+"'  " +
					"group by idov,nom_atrib) AS A " +
					"group by nom_atrib;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String Dato=rs.getObject("nom_atrib").toString();
					String Dato2=rs.getObject("repeticiones").toString();
					if (Dato!=null&&!Dato.isEmpty())
						{
						int repeticiones=Integer.parseInt(Dato2);
						ElementType_Texto.setHermanos(new ArrayList<CompleteTextElementType>());
						ElementType_Texto ATCUnidades=new ElementType_Texto(Dato, false, atributonumericotexto,LCole);
						atributonumericotexto.getSons().add(ATCUnidades.getExtendMetaControlled());
						ElementType_Texto.getHermanos().add(ATCUnidades.getExtendMetaControlled());
						ATCUnidades.ProcessAttributes();
						for (int i = 1; i < repeticiones; i++) {
							ATCUnidades=new ElementType_Texto(Dato, false, atributonumericotexto,ATCUnidades.getVocabulario(),LCole);
							atributonumericotexto.getSons().add(ATCUnidades.getExtendMetaControlled());
							ElementType_Texto.getHermanos().add(ATCUnidades.getExtendMetaControlled());
						}
						ATCUnidades.ProcessInstances();
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Funcion que procesa los hijos numericos de la categoria.
	 */
	private void prosessSonsNumericos() {
		try {
			//Calcula el numero u los atributos a designar :)
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT nom_atrib,max(cantidad) AS repeticiones FROM (" +
					"SELECT distinct idov, nom_atrib, count(*)" +
					"AS Cantidad " +
					"FROM atributos_numericos WHERE categoria='"+atributonumericotexto.getName()+"'  " +
					"group by idov,nom_atrib) AS A " +
					"group by nom_atrib;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String Dato=rs.getObject("nom_atrib").toString();
					String Dato2=rs.getObject("repeticiones").toString();
					if (Dato!=null&&!Dato.isEmpty())
						{
						ElementType_Numeric.setHermanos(new ArrayList<CompleteTextElementType>());
						ElementType_Numeric ATCUnidades=new ElementType_Numeric(Dato, false, atributonumericotexto,LCole);
						atributonumericotexto.getSons().add(ATCUnidades.getExtendMetaNumeric());
						ATCUnidades.ProcessAttributes();
						ElementType_Numeric.getHermanos().add(ATCUnidades.getExtendMetaNumeric());
						int repeticiones=Integer.parseInt(Dato2);
							for (int i = 1; i < repeticiones; i++) {
								ATCUnidades=new ElementType_Numeric(Dato, false, atributonumericotexto,ATCUnidades,LCole);
								atributonumericotexto.getSons().add(ATCUnidades.getExtendMetaNumeric());
								ATCUnidades.ProcessAttributes();
								ElementType_Numeric.getHermanos().add(ATCUnidades.getExtendMetaNumeric());
							}
							ATCUnidades.ProcessInstances();
						}
					
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * ProcessAttributes para esta clase ya que se compraten categorias entre dos tablas.
	 * @param tabla tabla a processar
	 */
	public void ProcessAttributes(Tabla tabla)
	{
		if (tabla==Tabla.ATRIBUTOS_TEXTO)
			prosessSonsTexto();
		else prosessSonsNumericos();
	}
	
	/**
	 * 
	 * @return the atributonumericotexto
	 */
	public CompleteElementType getMeta() {
		return atributonumericotexto;
	}

	@Override
	public void ProcessAttributes() {
				
	}

	@Override
	public void ProcessInstances() {
		
	}
}
