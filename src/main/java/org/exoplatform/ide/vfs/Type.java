/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.ide.vfs;

/**
 * Object types.
 * 
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id$
 */
public enum Type {
   DOCUMENT("document"), FOLDER("folder");

   private final String value;

   private Type(String value)
   {
      this.value = value;
   }

   /**
    * @return value of Type
    */
   public String value()
   {
      return value;
   }

   /**
    * Get Type instance from string value.
    * 
    * @param value string value
    * @return Type
    * @throws IllegalArgumentException if there is no corresponded Type for
    *            specified <code>value</code>
    */
   public static Type fromValue(String value)
   {
      for (Type e : Type.values())
         if (e.value.equals(value))
            return e;
      throw new IllegalArgumentException(value);
   }

   /**
    * @see java.lang.Enum#toString()
    */
   @Override
   public String toString()
   {
      return value;
   }
}