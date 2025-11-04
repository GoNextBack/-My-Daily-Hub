AI helped me build the BottomNavigation and manage the navigation state using currentBackStackEntryAsState(). It also gave me examples of how to keep state in ViewModel.
The misunderstanding happened when AI suggested using multiple NavHosts. That caused bugs in backstack behavior, so I changed it to one NavHost with popUpTo and launchSingleTop.
