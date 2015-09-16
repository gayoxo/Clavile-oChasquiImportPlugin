package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos.taxonomia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;

/**
 * Clase que parsea las taxonomias.
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Taxonomias implements InterfaceChasquiparser{

	private CompleteElementType atributosmetadatosTaxon;
	private HashMap<String, ElementType_Taxonomias_Taxonomia> Taxons;
	private LoadCollectionChasqui LCole;

	/**
	 * Constructor con parametros para el Meta
	 * @param name Nombre del Meta
	 * @param browseable Navegabilidad
	 * @param father
	 */
	public ElementType_Taxonomias(
			String name, boolean browseable, CompleteElementType father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributosmetadatosTaxon=new CompleteElementType(name, father);
		Taxons=new HashMap<String, ElementType_Taxonomias_Taxonomia>();
		
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);

		atributosmetadatosTaxon.getShows().add(Valor);
		atributosmetadatosTaxon.getShows().add(Valor2);
		atributosmetadatosTaxon.getShows().add(Valor3);

	}

	@Override
	public void ProcessAttributes() {
		processAllCategorias();
	}


	@Override
	public void ProcessInstances() {

		
	}
	
	/**
	 * Funcion que procesa todas las categorias.
	 */
	private void processAllCategorias() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT idov,num_ruta FROM metadatos WHERE (ruta= '/manifest/metadata/lom/classification/taxonpath/source/langstring' AND contenido='Sección/Sección');");
			if (rs!=null) 
			{
				while (rs.next()) {
					String idov=rs.getObject("idov").toString();
					String num_ruta=rs.getObject("num_ruta").toString();
					if (idov!=null&&num_ruta!=null&&!num_ruta.isEmpty()&&!idov.isEmpty())
						{
						findTaxonomy(idov,num_ruta);

						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Clase que encuentra la taxonomia en la que esta cada OV
	 * @param idov identificador del objeto virtual
	 * @param num_ruta ruta en la que esta la taxonomia.
	 */
	private void findTaxonomy(String idov, String num_ruta) {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct contenido,num_ruta FROM metadatos WHERE (ruta= '/manifest/metadata/lom/classification/taxonpath/taxon/entry/langstring' AND idov='"+idov+"');");
			if (rs!=null) 
			{
				while (rs.next()) {
					String contenido=rs.getObject("contenido").toString();
					String num_rutaValid=rs.getObject("num_ruta").toString();
					String[] DatosRutaSection=num_ruta.split("\\.");
					String[] DatosRutaEntry=num_rutaValid.split("\\.");
					if (contenido!=null&&num_rutaValid!=null&&!num_rutaValid.isEmpty()&&!contenido.isEmpty()&&DatosRutaSection[4].equals(DatosRutaEntry[4]))
						{
						ElementType_Taxonomias_Taxonomia existe=Taxons.get(contenido);
						if (existe!=null)
							{
							existe.setIdov(idov);
							existe.ProcessAttributes();
							//existe.setHermanos(new HashMap<String, ArrayList<Atributos_metadatos_Categoria_Taxonomias_Taxonomia_Nodo_ExtendMetaControlled>>());
							}
						else
							{
							existe=new ElementType_Taxonomias_Taxonomia(contenido, true, atributosmetadatosTaxon,idov,LCole);
							atributosmetadatosTaxon.getSons().add(existe.getExtendMeta());
							existe.setIdov(idov);
							existe.ProcessAttributes();
							Taxons.put(contenido, existe);
							}
						
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return the ExtendMeta
	 */
	public CompleteElementType getExtendMeta() {
		return atributosmetadatosTaxon;
	}

	

}
