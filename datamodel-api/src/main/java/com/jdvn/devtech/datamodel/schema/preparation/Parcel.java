package com.jdvn.devtech.datamodel.schema.preparation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Comment;
import org.locationtech.jts.geom.MultiPolygon;

import com.jdvn.devtech.datamodel.schema.DomainObject;
import com.jdvn.devtech.datamodel.schema.valuation.ValuationUnit;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parcel", schema = "preparation")
@Comment("Provides detailed information about valuation unit as parcel.")
public class Parcel extends DomainObject<String> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, columnDefinition = "character varying(40) DEFAULT public.uuid_generate_v1()")
	private String id;

	@Column(name = "area")
	@Comment("Legal area value that recorded in cadastre.")
	private Double area;

	@Comment("Code of land use.")
	private String curent_land_use;
	
	@Comment("Code of planed land use.")
	private String planed_land_use;
	
	@Column(name = "s_price")
	@Comment("Land price following the investigate in average per square meter.")
	private Double reasearchPrice;

	@Column(name = "f_price")
	@Comment("Land price in average per square meter in fact.")
	private Double decisionPrice;

	@Column(columnDefinition = "geometry NOT NULL")
	@Comment("Geometry of parcel for spatial displaying.")
	private MultiPolygon geom;

	@OneToOne
	@MapsId
	@JoinColumn(name = "id", foreignKey = @ForeignKey(name = "parcel_id_fkey"))
	private ValuationUnit valuation_unit;	
	/* Control many-to-many relationship between parcel and building
	 * as a parcel may contains many buildings and a building may
	 * located at few parcels */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "parcels_buildings_links", schema = "preparation", joinColumns = @JoinColumn(name = "parcel_id"), inverseJoinColumns = @JoinColumn(name = "building_id"), foreignKey = @ForeignKey(name = "parcels_buildings_links_parcel_id_fkey"), inverseForeignKey = @ForeignKey(name = "parcels_buildings_links_building_id_fkey"))
	private List<Building> buildings = new ArrayList<>();
		
	@Override
	public String getId() {
		return id;
	}

	@Override
	public String print() {
		return id;
	}

}