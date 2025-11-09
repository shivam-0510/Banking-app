# How to Access Admin Dashboard

## Prerequisites
To access the admin dashboard, you need a user account with the `ADMIN` role.

## Method 1: Assign ADMIN Role via Database (Quickest)

1. **Connect to your PostgreSQL database:**
   ```bash
   psql -U postgres -d banking_auth_db
   ```

2. **Find your user ID:**
   ```sql
   SELECT id, username, email FROM users WHERE email = 'your-email@example.com';
   ```

3. **Add ADMIN role to the user:**
   ```sql
   INSERT INTO user_roles (user_id, role) 
   VALUES ((SELECT id FROM users WHERE email = 'your-email@example.com'), 'ADMIN')
   ON CONFLICT DO NOTHING;
   ```

   Or if you want to add both USER and ADMIN roles:
   ```sql
   INSERT INTO user_roles (user_id, role) VALUES 
   ((SELECT id FROM users WHERE email = 'your-email@example.com'), 'USER'),
   ((SELECT id FROM users WHERE email = 'your-email@example.com'), 'ADMIN')
   ON CONFLICT DO NOTHING;
   ```

4. **Verify the role was added:**
   ```sql
   SELECT u.username, u.email, ur.role 
   FROM users u 
   LEFT JOIN user_roles ur ON u.id = ur.user_id 
   WHERE u.email = 'your-email@example.com';
   ```

## Method 2: Update Backend to Support Role Assignment

If you want to create admin users through the API, you'll need to update the backend to support role assignment in `UserCreationRequest` and `UserService.createUser()`.

## Accessing the Admin Dashboard

Once you have the ADMIN role:

1. **Logout and login again** (to refresh the JWT token with the new role)

2. **Access the admin dashboard:**
   - **URL:** `http://localhost:3000/admin`
   - **Navigation:** Click the "Admin" link in the navbar (only visible to admin users)
   - **Direct access:** You can also navigate directly to `/admin` in the URL

3. **What you'll see:**
   - Overview tab with statistics
   - Users tab for managing all users
   - Accounts tab for managing all accounts

## Features Available in Admin Dashboard

- **User Management:**
  - View all users
  - Create new users
  - Delete users
  - Activate/Deactivate users
  - Search users

- **Account Management:**
  - View all accounts
  - Update account status (Activate/Deactivate)
  - Search accounts

- **Statistics:**
  - Total users count
  - Active users count
  - Total accounts count
  - Active accounts count
  - Total balance across all accounts

## Troubleshooting

**If you don't see the Admin link:**
- Make sure you've assigned the ADMIN role in the database
- Logout and login again to refresh your JWT token
- Check browser console for any errors
- Verify the role in the database: `SELECT * FROM user_roles WHERE role = 'ADMIN';`

**If you get "Access Denied" or redirected to dashboard:**
- Your JWT token might not have the updated roles
- Logout completely and login again
- Clear browser localStorage and login again

## Quick SQL Script

Here's a complete SQL script to make a user an admin:

```sql
-- Replace 'your-email@example.com' with your actual email
DO $$
DECLARE
    user_id_val BIGINT;
BEGIN
    SELECT id INTO user_id_val FROM users WHERE email = 'your-email@example.com';
    
    IF user_id_val IS NOT NULL THEN
        INSERT INTO user_roles (user_id, role) 
        VALUES (user_id_val, 'ADMIN')
        ON CONFLICT DO NOTHING;
        
        RAISE NOTICE 'Admin role added to user with ID: %', user_id_val;
    ELSE
        RAISE NOTICE 'User not found!';
    END IF;
END $$;
```

