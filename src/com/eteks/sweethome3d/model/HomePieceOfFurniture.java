/*
 * HomePieceOfFurniture.java 15 mai 2006
 *
 * Copyright (c) 2006 Emmanuel PUYBARET / eTeks <info@eteks.com>. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.eteks.sweethome3d.model;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A piece of furniture in {@link Home home}.
 * @author Emmanuel Puybaret
 */
public class HomePieceOfFurniture implements PieceOfFurniture, Selectable {
  private static final long serialVersionUID = 1L;
  
  /** 
   * Properties on which home furniture may be sorted.  
   */
  public enum SortableProperty {CATALOG_ID, NAME, WIDTH, DEPTH, HEIGHT, MOVABLE, 
                                DOOR_OR_WINDOW, COLOR, VISIBLE, X, Y, ELEVATION, ANGLE,
                                PRICE, VALUE_ADDED_TAX, VALUE_ADDED_TAX_PERCENTAGE, PRICE_VALUE_ADDED_TAX_INCLUDED};
  private static final Map<SortableProperty, Comparator<HomePieceOfFurniture>> SORTABLE_PROPERTY_COMPARATORS;
  private static final float [][] IDENTITY = new float [][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
  
  static {
    final Collator collator = Collator.getInstance();
    // Init piece property comparators
    SORTABLE_PROPERTY_COMPARATORS = new HashMap<SortableProperty, Comparator<HomePieceOfFurniture>>();
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.CATALOG_ID, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          if (piece1.catalogId == null) {
            return -1;
          } else if (piece2.catalogId == null) {
            return 1; 
          } else {
            return collator.compare(piece1.catalogId, piece2.catalogId);
          }
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.NAME, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return collator.compare(piece1.name, piece2.name);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.WIDTH, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.width, piece2.width);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.HEIGHT, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.height, piece2.height);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.DEPTH, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.depth, piece2.depth);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.MOVABLE, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.movable, piece2.movable);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.DOOR_OR_WINDOW, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.doorOrWindow, piece2.doorOrWindow);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.COLOR, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          if (piece1.color == null) {
            return -1;
          } else if (piece2.color == null) {
            return 1; 
          } else {
            return piece1.color - piece2.color;
          }
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.VISIBLE, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.visible, piece2.visible);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.X, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.x, piece2.x);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.Y, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.y, piece2.y);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.ELEVATION, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.elevation, piece2.elevation);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.ANGLE, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.angle, piece2.angle);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.PRICE, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.price, piece2.price);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.VALUE_ADDED_TAX_PERCENTAGE, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.valueAddedTaxPercentage, piece2.valueAddedTaxPercentage);
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.VALUE_ADDED_TAX, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.getValueAddedTax(), piece2.getValueAddedTax());
        }
      });
    SORTABLE_PROPERTY_COMPARATORS.put(SortableProperty.PRICE_VALUE_ADDED_TAX_INCLUDED, new Comparator<HomePieceOfFurniture>() {
        public int compare(HomePieceOfFurniture piece1, HomePieceOfFurniture piece2) {
          return HomePieceOfFurniture.compare(piece1.getPriceValueAddedTaxIncluded(), piece2.getPriceValueAddedTaxIncluded());
        }
      });
  }
  
  private static int compare(float value1, float value2) {
    return value1 < value2 
               ? -1
               : (value1 == value2
                   ? 0 : 1);
  }
  
  private static int compare(boolean value1, boolean value2) {
    return value1 == value2 
               ? 0
               : (value1 ? -1 : 1);
  }
  
  private static int compare(BigDecimal value1, BigDecimal value2) {
    if (value1 == null) {
      return -1;
    } else if (value2 == null) {
      return 1; 
    } else {
      return value1.compareTo(value2);
    }
  }
  
  private String     catalogId;
  private String     name;
  private Content    icon;
  private Content    model;
  private float      width;
  private float      depth;
  private float      height;
  private float      elevation;
  private boolean    movable;
  private boolean    doorOrWindow;
  private Integer    color;
  private float [][] modelRotation;
  private boolean    backFaceShown;
  private boolean    resizable;
  private BigDecimal price;
  private BigDecimal valueAddedTaxPercentage;
  private boolean    visible;
  private float      x;
  private float      y;
  private float      angle;
  private boolean    modelMirrored;

  private transient Shape shapeCache;

  /**
   * Creates a home piece of furniture from an existing piece.
   * @param piece the piece from which data are copied
   */
  public HomePieceOfFurniture(PieceOfFurniture piece) {
    this.name = piece.getName();
    this.icon = piece.getIcon();
    this.model = piece.getModel();
    this.width = piece.getWidth();
    this.depth = piece.getDepth();
    this.height = piece.getHeight();
    this.elevation = piece.getElevation();
    this.movable = piece.isMovable();
    this.doorOrWindow = piece.isDoorOrWindow();
    this.color = piece.getColor();
    this.modelRotation = piece.getModelRotation();
    this.backFaceShown = piece.isBackFaceShown();
    this.resizable = piece.isResizable();
    this.price = piece.getPrice();
    this.valueAddedTaxPercentage = piece.getValueAddedTaxPercentage();
    if (piece instanceof HomePieceOfFurniture) {
      HomePieceOfFurniture homePiece = 
          (HomePieceOfFurniture)piece;
      this.catalogId = homePiece.getCatalogId();
      this.visible = homePiece.isVisible();
      this.angle = homePiece.getAngle();
      this.x = homePiece.getX();
      this.y = homePiece.getY();
      this.modelMirrored = homePiece.isModelMirrored();
    } else {
      if (piece instanceof CatalogPieceOfFurniture) {
        this.catalogId = ((CatalogPieceOfFurniture)piece).getId();
      }      
      this.visible = true;
      this.x = this.width / 2;
      this.y = this.depth / 2;
    }
  }

  /**
   * Initializes new piece fields to their default values 
   * and reads home from <code>in</code> stream with default reading method.
   */
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    this.modelRotation = IDENTITY;
    this.resizable = true;
    in.defaultReadObject();
  }

  /**
   * Returns the catalog ID of this piece of furniture or <code>null</code>.
   */
  public String getCatalogId() {
    return this.catalogId;
  }
  
  /**
   * Returns the name of this piece of furniture.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name of this piece of furniture.
   * This method should be called from {@link Home}, which
   * controls notifications when a piece changed.
   */
  void setName(String name) {
    this.name = name;
  }
   
  /**
   * Returns the depth of this piece of furniture.
   */
  public float getDepth() {
    return this.depth;
  }

  /**
   * Sets the depth of this piece of furniture.
   * This method should be called from {@link Home}, which
   * controls notifications when a piece changed.
   * @throws IllegalStateException if this piece of furniture isn't resizable
   */
  void setDepth(float depth) {
    if (this.resizable) {
      this.depth = depth;
      this.shapeCache = null;
    } else {
      throw new IllegalStateException("Piece isn't resizable");
    }
  }

  /**
   * Returns the height of this piece of furniture.
   */
  public float getHeight() {
    return this.height;
  }

  /**
   * Sets the height of this piece of furniture.
   * This method should be called from {@link Home}, which
   * controls notifications when a piece changed.
   * @throws IllegalStateException if this piece of furniture isn't resizable
   */
  void setHeight(float height) {
    if (this.resizable) {
      this.height = height;
      this.shapeCache = null;
    } else {
      throw new IllegalStateException("Piece isn't resizable");
    }
  }

  /**
   * Returns the width of this piece of furniture.
   */
  public float getWidth() {
    return this.width;
  }

  /**
   * Sets the width of this piece of furniture.
   * This method should be called from {@link Home}, which
   * controls notifications when a piece changed.
   * @throws IllegalStateException if this piece of furniture isn't resizable
   */
  void setWidth(float width) {
    if (this.resizable) {
      this.width = width;
      this.shapeCache = null;
    } else {
      throw new IllegalStateException("Piece isn't resizable");
    }
  }

  /**
   * Returns the elevation of the bottom of this piece of furniture. 
   */
  public float getElevation() {
    return this.elevation;
  }

  /**
   * Sets the elevation of this piece of furniture.
   * This method should be called from {@link Home}, which
   * controls notifications when a piece changed.
   */
  void setElevation(float elevation) {
    this.elevation = elevation;
  }

  /**
   * Returns <code>true</code> if this piece of furniture is movable.
   */
  public boolean isMovable() {
    return this.movable;
  }

  /**
   * Returns <code>true</code> if this piece of furniture is a door or a window.
   */
  public boolean isDoorOrWindow() {
    return this.doorOrWindow;
  }

  /**
   * Returns the icon of this piece of furniture.
   */
  public Content getIcon() {
    return this.icon;
  }

  /**
   * Returns the 3D model of this piece of furniture.
   */
  public Content getModel() {
    return this.model;
  }
  
  /**
   * Returns the color of this piece of furniture.
   * @return the color of the piece as RGB code or <code>null</code> if piece color is unchanged.
   */
  public Integer getColor() {
    return this.color;
  }
  
  /**
   * Sets the color of this piece of furniture or <code>null</code> if piece color is unchanged.
   * This method should be called from {@link Home}, which
   * controls notifications when a piece changed.
   */
  void setColor(Integer color) {
    this.color = color;
  }

  /**
   * Returns <code>true</code> if this piece is resizable.
   */
  public boolean isResizable() {
    return this.resizable;    
  }
  
  /**
   * Returns the price of this piece of furniture or <code>null</code>. 
   */
  public BigDecimal getPrice() {
    return this.price;
  }
  
  /**
   * Returns the Value Added Tax percentage applied to the price of this piece of furniture. 
   */
  public BigDecimal getValueAddedTaxPercentage() {
    return this.valueAddedTaxPercentage;
  }

  /**
   * Returns the Value Added Tax applied to the price of this piece of furniture. 
   */
  public BigDecimal getValueAddedTax() {
    if (this.price != null && this.valueAddedTaxPercentage != null) {
      return this.price.multiply(this.valueAddedTaxPercentage).
          setScale(this.price.scale(), RoundingMode.HALF_UP);
    } else {
      return null;
    }
  }

  /**
   * Returns the price of this piece of furniture, Value Added Tax included. 
   */
  public BigDecimal getPriceValueAddedTaxIncluded() {
    if (this.price != null && this.valueAddedTaxPercentage != null) {
      return this.price.add(getValueAddedTax());
    } else {
      return null;
    }
  }

  /**
   * Returns <code>true</code> if this piece of furniture is visible.
   */
  public boolean isVisible() {
    return this.visible;
  }
  
  /**
   * Sets whether this piece of furniture is visible or not.
   * This method should be called from {@link Home}, which
   * controls notifications when a piece changed.
   */
  void setVisible(boolean visible) {
    this.visible = visible;
  }

  /**
   * Returns the abscissa of the center of this piece of furniture.
   */
  public float getX() {
    return this.x;
  }

  /**
   * Sets the abscissa of the center of this piece.
   * This method should be called from {@link Home}, which
   * controls notifications when a piece changed.
   */
  void setX(float x) {
    this.x = x;
    this.shapeCache = null;
  }
  
  /**
   * Returns the ordinate of the center of this piece of furniture.
   */
  public float getY() {
    return this.y;
  }

  /**
   * Sets the ordinate of the center of this piece.
   * This method should be called from {@link Home}, which
   * controls notifications when a piece changed.
   */
  void setY(float y) {
    this.y = y;
    this.shapeCache = null;
  }

  /**
   * Returns the angle in radians of this piece of furniture. 
   */
  public float getAngle() {
    return this.angle;
  }

  /**
   * Sets the angle of this piece.
   * This method should be called from {@link Home}, which
   * controls notifications when a piece changed.
   */
  void setAngle(float angle) {
    this.angle = angle;
    this.shapeCache = null;
  }

  /**
   * Returns <code>true</code> if the model of this piece should be mirrored.
   */
  public boolean isModelMirrored() {
    return this.modelMirrored;
  }

  /**
   * Sets whether the model of this piece of furniture is mirrored or not.
   * This method should be called from {@link Home}, which
   * controls notifications when a piece changed.
   * @throws IllegalStateException if this piece of furniture isn't resizable
   */
  void setModelMirrored(boolean modelMirrored) {
    if (this.resizable) {
      this.modelMirrored = modelMirrored;
    } else {
      throw new IllegalStateException("Piece isn't resizable");
    }
  }

  /**
   * Returns the rotation 3 by 3 matrix of this piece of furniture that ensures 
   * its model is correctly oriented.
   */
  public float [][] getModelRotation() {
    // Return a deep copy to avoid any misuse of piece data
    return new float [][] {{this.modelRotation[0][0], this.modelRotation[0][1], this.modelRotation[0][2]},
                           {this.modelRotation[1][0], this.modelRotation[1][1], this.modelRotation[1][2]},
                           {this.modelRotation[2][0], this.modelRotation[2][1], this.modelRotation[2][2]}};
  }

  /**
   * Returns <code>true</code> if the back face of the piece of furniture
   * model should be displayed.
   */
  public boolean isBackFaceShown() {
    return this.backFaceShown;
  }
  
  /**
   * Returns the points of each corner of a piece.
   * @return an array of the 4 (x,y) coordinates of the piece corners.
   */
  public float [][] getPoints() {
    float [][] piecePoints = new float[4][2];
    PathIterator it = getShape().getPathIterator(null);
    for (int i = 0; i < piecePoints.length; i++) {
      it.currentSegment(piecePoints [i]);
      it.next();
    }
    return piecePoints;
  }
  
  /**
   * Returns <code>true</code> if this piece intersects
   * with the horizontal rectangle which opposite corners are at points
   * (<code>x0</code>, <code>y0</code>) and (<code>x1</code>, <code>y1</code>).
   */
  public boolean intersectsRectangle(float x0, float y0, 
                                     float x1, float y1) {
    Rectangle2D rectangle = new Rectangle2D.Float(x0, y0, 0, 0);
    rectangle.add(x1, y1);
    return getShape().intersects(rectangle);
  }
  
  /**
   * Returns <code>true</code> if this piece contains 
   * the point at (<code>x</code>, <code>y</code>)
   * with a given <code>margin</code>.
   */
  public boolean containsPoint(float x, float y, float margin) {
    return getShape().intersects(x - margin, y - margin, 2 * margin, 2 * margin);
  }
  
  /**
   * Returns <code>true</code> if one of the vertex of this piece is 
   * the point at (<code>x</code>, <code>y</code>)
   * with a given <code>margin</code>.
   */
  public boolean isVertexAt(float x, float y, float margin) {
    for (float [] point : getPoints()) {
      if (Math.abs(x - point[0]) <= margin && Math.abs(y - point[1]) <= margin) {
        return true;
      }
    } 
    return false;
  }

  /**
   * Returns <code>true</code> if the top left vertex of this piece is 
   * the point at (<code>x</code>, <code>y</code>)
   * with a given <code>margin</code>.
   */
  public boolean isTopLeftVertexAt(float x, float y, float margin) {
    float [][] points = getPoints();
    return Math.abs(x - points[0][0]) <= margin && Math.abs(y - points[0][1]) <= margin;
  }

  /**
   * Returns <code>true</code> if the top right vertex of this piece is 
   * the point at (<code>x</code>, <code>y</code>)
   * with a given <code>margin</code>.
   */
  public boolean isTopRightVertexAt(float x, float y, float margin) {
    float [][] points = getPoints();
    return Math.abs(x - points[1][0]) <= margin && Math.abs(y - points[1][1]) <= margin;
  }

  /**
   * Returns <code>true</code> if the bottom left vertex of this piece is 
   * the point at (<code>x</code>, <code>y</code>)
   * with a given <code>margin</code>.
   */
  public boolean isBottomLeftVertexAt(float x, float y, float margin) {
    float [][] points = getPoints();
    return Math.abs(x - points[3][0]) <= margin && Math.abs(y - points[3][1]) <= margin;
  }

  /**
   * Returns <code>true</code> if the bottom right vertex of this piece is 
   * the point at (<code>x</code>, <code>y</code>)
   * with a given <code>margin</code>.
   */
  public boolean isBottomRightVertexAt(float x, float y, float margin) {
    float [][] points = getPoints();
    return Math.abs(x - points[2][0]) <= margin && Math.abs(y - points[2][1]) <= margin;
  }

  /**
   * Returns the shape matching this piece.
   */
  private Shape getShape() {
    if (this.shapeCache == null) {
      // Create the rectangle that matches piece bounds
      Rectangle2D pieceRectangle = new Rectangle2D.Float(
          this.x - this.width / 2,
          this.y - this.depth / 2,
          this.width, this.depth);
      // Apply rotation to the rectangle
      AffineTransform rotation = new AffineTransform();
      rotation.setToRotation(this.angle, this.x, this.y);
      PathIterator it = pieceRectangle.getPathIterator(rotation);
      GeneralPath pieceShape = new GeneralPath();
      pieceShape.append(it, false);
      // Cache shape
      this.shapeCache = pieceShape;
    }
    return this.shapeCache;
  }
  
  /**
   * Returns a comparator that compares furniture on a given <code>property</code> in ascending order.
   */
  public static Comparator<HomePieceOfFurniture> getFurnitureComparator(SortableProperty property) {
    return SORTABLE_PROPERTY_COMPARATORS.get(property);    
  }
}
