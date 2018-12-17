/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2018 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.businessproduction.service;

import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.service.administration.SequenceService;
import com.axelor.apps.production.db.BillOfMaterial;
import com.axelor.apps.production.db.ProductionOrder;
import com.axelor.apps.production.db.repo.ProductionOrderRepository;
import com.axelor.apps.production.service.ManufOrderService;
import com.axelor.apps.production.service.ProductionOrderServiceImpl;
import com.axelor.apps.project.db.Project;
import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.exception.AxelorException;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductionOrderServiceBusinessImpl extends ProductionOrderServiceImpl {

  @Inject
  public ProductionOrderServiceBusinessImpl(
      ManufOrderService manufOrderService,
      SequenceService sequenceService,
      ProductionOrderRepository productionOrderRepo) {
    super(manufOrderService, sequenceService, productionOrderRepo);
  }

  @Transactional(rollbackOn = {AxelorException.class, Exception.class})
  public ProductionOrder generateProductionOrder(
      Product product,
      BillOfMaterial billOfMaterial,
      BigDecimal qtyRequested,
      Project project,
      LocalDateTime startDate,
      SaleOrder saleOrder)
      throws AxelorException {

    ProductionOrder productionOrder = this.createProductionOrder(saleOrder);
    productionOrder.setProject(project);

    this.addManufOrder(
        productionOrder,
        product,
        billOfMaterial,
        qtyRequested,
        startDate,
        saleOrder,
        ManufOrderService.ORIGIN_TYPE_OTHER);

    return productionOrderRepo.save(productionOrder);
  }
}
