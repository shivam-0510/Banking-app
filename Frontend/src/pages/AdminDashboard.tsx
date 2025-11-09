import { useState, useEffect } from 'react';
import { Layout } from '../components/Layout';
import { apiService } from '../services/api';
import type { UserResponse, Account } from '../types';
import { Users, Wallet, TrendingUp, Activity, Shield, DollarSign } from 'lucide-react';
import { UserManagement } from '../components/admin/UserManagement';
import { AccountManagement } from '../components/admin/AccountManagement';

export const AdminDashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'overview' | 'users' | 'accounts'>('overview');
  const [stats, setStats] = useState({
    totalUsers: 0,
    activeUsers: 0,
    totalAccounts: 0,
    activeAccounts: 0,
    totalBalance: 0,
    totalTransactions: 0,
  });
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      setIsLoading(true);
      const [users, accounts] = await Promise.all([
        apiService.getAllUsers(),
        apiService.getAllAccounts().catch(() => []), // Handle if endpoint doesn't exist
      ]);

      const activeUsers = users.filter((u) => u.active).length;
      const activeAccounts = accounts.filter((a) => a.isActive).length;
      const totalBalance = accounts.reduce((sum, acc) => sum + (parseFloat(acc.balance.toString()) || 0), 0);

      setStats({
        totalUsers: users.length,
        activeUsers,
        totalAccounts: accounts.length,
        activeAccounts,
        totalBalance,
        totalTransactions: 0, // Would need a separate endpoint
      });
    } catch (error) {
      console.error('Error loading stats:', error);
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) {
    return (
      <Layout>
        <div className="flex items-center justify-center min-h-screen">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="px-4 sm:px-0">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
          <p className="mt-2 text-sm text-gray-600">Manage users, accounts, and monitor system activity</p>
        </div>

        {/* Tabs */}
        <div className="border-b border-gray-200 mb-6">
          <nav className="-mb-px flex space-x-8">
            <button
              onClick={() => setActiveTab('overview')}
              className={`${
                activeTab === 'overview'
                  ? 'border-primary-500 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              } whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm`}
            >
              Overview
            </button>
            <button
              onClick={() => setActiveTab('users')}
              className={`${
                activeTab === 'users'
                  ? 'border-primary-500 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              } whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm`}
            >
              Users
            </button>
            <button
              onClick={() => setActiveTab('accounts')}
              className={`${
                activeTab === 'accounts'
                  ? 'border-primary-500 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              } whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm`}
            >
              Accounts
            </button>
          </nav>
        </div>

        {/* Content */}
        {activeTab === 'overview' && (
          <div>
            {/* Statistics Cards */}
            <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3 mb-8">
              <div className="bg-white overflow-hidden shadow rounded-lg">
                <div className="p-5">
                  <div className="flex items-center">
                    <div className="flex-shrink-0">
                      <Users className="h-6 w-6 text-primary-600" />
                    </div>
                    <div className="ml-5 w-0 flex-1">
                      <dl>
                        <dt className="text-sm font-medium text-gray-500 truncate">Total Users</dt>
                        <dd className="text-lg font-medium text-gray-900">{stats.totalUsers}</dd>
                        <dd className="text-sm text-gray-500">{stats.activeUsers} active</dd>
                      </dl>
                    </div>
                  </div>
                </div>
              </div>

              <div className="bg-white overflow-hidden shadow rounded-lg">
                <div className="p-5">
                  <div className="flex items-center">
                    <div className="flex-shrink-0">
                      <Wallet className="h-6 w-6 text-green-600" />
                    </div>
                    <div className="ml-5 w-0 flex-1">
                      <dl>
                        <dt className="text-sm font-medium text-gray-500 truncate">Total Accounts</dt>
                        <dd className="text-lg font-medium text-gray-900">{stats.totalAccounts}</dd>
                        <dd className="text-sm text-gray-500">{stats.activeAccounts} active</dd>
                      </dl>
                    </div>
                  </div>
                </div>
              </div>

              <div className="bg-white overflow-hidden shadow rounded-lg">
                <div className="p-5">
                  <div className="flex items-center">
                    <div className="flex-shrink-0">
                      <DollarSign className="h-6 w-6 text-blue-600" />
                    </div>
                    <div className="ml-5 w-0 flex-1">
                      <dl>
                        <dt className="text-sm font-medium text-gray-500 truncate">Total Balance</dt>
                        <dd className="text-lg font-medium text-gray-900">
                          ₹{stats.totalBalance.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                        </dd>
                      </dl>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            {/* Quick Actions */}
            <div className="bg-white shadow rounded-lg p-6">
              <h2 className="text-lg font-medium text-gray-900 mb-4">Quick Actions</h2>
              <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
                <button
                  onClick={() => setActiveTab('users')}
                  className="flex items-center p-4 border border-gray-300 rounded-lg hover:bg-gray-50 transition"
                >
                  <Users className="h-8 w-8 text-primary-600 mr-4" />
                  <div className="text-left">
                    <p className="font-medium text-gray-900">Manage Users</p>
                    <p className="text-sm text-gray-500">View and manage all users</p>
                  </div>
                </button>
                <button
                  onClick={() => setActiveTab('accounts')}
                  className="flex items-center p-4 border border-gray-300 rounded-lg hover:bg-gray-50 transition"
                >
                  <Wallet className="h-8 w-8 text-green-600 mr-4" />
                  <div className="text-left">
                    <p className="font-medium text-gray-900">Manage Accounts</p>
                    <p className="text-sm text-gray-500">View and manage all accounts</p>
                  </div>
                </button>
                <div className="flex items-center p-4 border border-gray-300 rounded-lg bg-gray-50">
                  <Activity className="h-8 w-8 text-blue-600 mr-4" />
                  <div className="text-left">
                    <p className="font-medium text-gray-900">System Activity</p>
                    <p className="text-sm text-gray-500">Coming soon</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'users' && <UserManagement onRefresh={loadStats} />}
        {activeTab === 'accounts' && <AccountManagement onRefresh={loadStats} />}
      </div>
    </Layout>
  );
};

