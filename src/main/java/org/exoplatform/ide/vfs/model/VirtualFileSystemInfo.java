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
package org.exoplatform.ide.vfs.model;

import org.exoplatform.ide.vfs.ObjectId;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Describe virtual file system and its capabilities.
 * 
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: VirtualFileSystemInfo.java 63654 2010-12-06 09:49:34Z andrew00x
 *          $
 */
public class VirtualFileSystemInfo
{
   /**
    * ACL capabilities.
    */
   public enum ACLCapability {
      /** ACL is not supported. */
      NONE("none"),
      /**
       * ACL may be only discovered but can't be changed over virtual file
       * system API.
       */
      READ("read"),
      /** ACL may be discovered and managed. */
      MANAGE("manage");

      private final String value;

      private ACLCapability(String value)
      {
         this.value = value;
      }

      /**
       * @return value of ACLCapabilities
       */
      public String value()
      {
         return value;
      }

      /**
       * Get ACLCapabilities instance from string value.
       * 
       * @param value string value
       * @return ACLCapabilities
       * @throws IllegalArgumentException if there is no corresponded
       *            ACLCapabilities for specified <code>value</code>
       */
      public static ACLCapability fromValue(String value)
      {
         for (ACLCapability e : ACLCapability.values())
            if (e.value.equals(value))
               return e;
         throw new IllegalArgumentException(value);
      }

      /**
       * @see java.lang.Enum#toString()
       */
      public String toString()
      {
         return value;
      }
   }

   /**
    * Query capabilities.
    */
   public enum QueryCapability {
      /** Query is not supported. */
      NONE("none"),
      /** Query supported for properties only. */
      PROPERTIES("properties"),
      /** Full text search supported only. */
      FULLTEXT("fulltext"),
      /** Both queries are supported but not in one statement. */
      BOTHSEPARATE("bothseparate"),
      /** Both queries are supported in one statement. */
      BOTHCOMBINED("bothcombined");

      private final String value;

      private QueryCapability(String value)
      {
         this.value = value;
      }

      /**
       * @return value of QueryCapability
       */
      public String value()
      {
         return value;
      }

      /**
       * Get QueryCapability instance from string value.
       * 
       * @param value string value
       * @return QueryCapability
       * @throws IllegalArgumentException if there is no corresponded
       *            QueryCapability for specified <code>value</code>
       */
      public static QueryCapability fromValue(String value)
      {
         for (QueryCapability e : QueryCapability.values())
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

   /**
    * Locking capabilities.
    */
   public enum LockCapability {
      /** Locking is not supported. */
      NONE("none"),
      /** Lock may be placed on object itself. Deep locking is not supported. */
      OBJECT("object"),
      /** Deep locking supported. */
      DEEP("deep");

      private final String value;

      private LockCapability(String value)
      {
         this.value = value;
      }

      /**
       * @return value of LockCapability
       */
      public String value()
      {
         return value;
      }

      /**
       * Get LockCapability instance from string value.
       * 
       * @param value string value
       * @return LockCapability
       * @throws IllegalArgumentException if there is no corresponded
       *            LockCapability for specified <code>value</code>
       */
      public static LockCapability fromValue(String value)
      {
         for (LockCapability e : LockCapability.values())
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

   /**
    * Basic permissions.
    */
   public enum BasicPermissions {
      /** Read permission. */
      READ("read"),
      /** Write permission. */
      WRITE("write"),
      /** All. Any operation allowed. */
      ALL("all");

      private final String value;

      private BasicPermissions(String value)
      {
         this.value = value;
      }

      /**
       * @return value of BasicPermissions
       */
      public String value()
      {
         return value;
      }

      /**
       * Get BasicPermissions instance from string value.
       * 
       * @param value string value
       * @return BasicPermissions
       * @throws IllegalArgumentException if there is no corresponded
       *            BasicPermissions for specified <code>value</code>
       */
      public static BasicPermissions fromValue(String value)
      {
         for (BasicPermissions e : BasicPermissions.values())
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

   public static final String ANONYMOUS_PRINCIPAL = "anonymous";

   private boolean versioningSupported;

   private String anonymousPrincipal;

   private Collection<String> permissions;

   private LockCapability lockCapability;

   private ACLCapability aclCapability;

   private QueryCapability queryCapability;

   private ObjectId rootFolderId;

   private String rootFolderPath;

   public VirtualFileSystemInfo(boolean versioningSupported, String anonymousPrincipal, Collection<String> permissions,
      LockCapability lockCapability, ACLCapability aclCapability, QueryCapability queryCapability,
      ObjectId rootFolderId, String rootFolderPath)
   {
      this.versioningSupported = versioningSupported;
      this.anonymousPrincipal = anonymousPrincipal;
      this.permissions = permissions;
      this.lockCapability = lockCapability;
      this.aclCapability = aclCapability;
      this.queryCapability = queryCapability;
   }

   public VirtualFileSystemInfo()
   {
      this(false, ANONYMOUS_PRINCIPAL, new ArrayList<String>(), LockCapability.NONE, ACLCapability.NONE,
         QueryCapability.NONE, null, null);
   }

   public boolean isVersioningSupported()
   {
      return versioningSupported;
   }

   public void setVersioningSupported(boolean versioningSupported)
   {
      this.versioningSupported = versioningSupported;
   }

   public String getAnonymousPrincipal()
   {
      return anonymousPrincipal;
   }

   public void setAnonymousPrincipal(String anonymousPrincipal)
   {
      this.anonymousPrincipal = anonymousPrincipal;
   }

   public Collection<String> getPermissions()
   {
      return permissions;
   }

   public void setPermissions(Collection<String> permissions)
   {
      this.permissions = permissions;
   }

   public LockCapability getLockCapability()
   {
      return lockCapability;
   }

   public void setLockSupported(LockCapability lockCapability)
   {
      this.lockCapability = lockCapability;
   }

   public ACLCapability getAclCapability()
   {
      return aclCapability;
   }

   public void setAclCapability(ACLCapability aclCapability)
   {
      this.aclCapability = aclCapability;
   }

   public QueryCapability getQueryCapability()
   {
      return queryCapability;
   }

   public void setQueryCapability(QueryCapability queryCapability)
   {
      this.queryCapability = queryCapability;
   }

   public ObjectId getRootFolderId()
   {
      return rootFolderId;
   }

   public void setRootFolderId(ObjectId rootFolderId)
   {
      this.rootFolderId = rootFolderId;
   }

   public String getRootFolderPath()
   {
      return rootFolderPath;
   }

   public void setRootFolderPath(String rootFolderPath)
   {
      this.rootFolderPath = rootFolderPath;
   }
}
