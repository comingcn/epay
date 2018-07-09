package com.epay.xj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.epay.xj.domain.CertNoDO;

@Mapper
public interface CertListMapper {
	
	public List<CertNoDO> getAll();

}
