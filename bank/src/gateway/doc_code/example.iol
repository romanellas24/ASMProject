type User { name: string email: string karma: int }
type UserWithUsername { username: string name: string email: string karma: int }
type ListUsersRequest { minKarma?: int }
type ListUsersResponse { usernames*: string }
type UserRequest { username: string }

interface UsersInterface {
RequestResponse:
    createUser( UserWithUsername )( void ) throws UserExists( string ),
    listUsers( ListUsersRequest )( ListUsersResponse ),
    viewUser( UserRequest )( User ) throws UserNotFound( string ),
    updateUser( UserWithUsername )( void ) throws UserNotFound( string ),
    deleteUser( UserRequest )( void ) throws UserNotFound( string ),
    default( undefined )( string )
}

service App {
    execution: sequential

    inputPort Web {
        location: "socket://localhost:8080"
        protocol: http {
            format = "json"
            osc << {
                createUser << {
                    template = "/api/user"
                    method = "post"
                    statusCodes = 201 // 201 = Created
                    statusCodes.TypeMismatch = 400
                    statusCodes.UserExists = 400
                    response.headers -> responseHeaders
                }
                listUsers << {
                    template = "/api/user"
                    method = "get"
                }
                viewUser << {
                    template = "/api/user/{username}"
                    method = "get"
                    statusCodes.UserNotFound = 404
                }
                updateUser << {
                    template = "/api/user/{username}"
                    method = "put"
                    statusCodes.TypeMismatch = 400
                    statusCodes.UserNotFound = 404
                }
                deleteUser << {
                    template = "/api/user/{username}"
                    method = "delete"
                    statusCodes.UserNotFound = 404
                }
            }
            default = "default"
        }
        interfaces: UsersInterface
    }

    init {
        global.users << {
            john << {
                name = "John Doe", email = "john@doe.com", karma = 4
            }
            jane << {
                name = "Jane Doe", email = "jane@doe.com", karma = 6
            }
        }
    }

    main {
        [ createUser( request )( ) {
            if( is_defined( global.users.(request.username) ) ) {
                throw( UserExists, request.username )
            } else {
                global.users.(request.username) << request
                undef( global.users.(request.username).username )
                responseHeaders.Location = "/api/user/" + request.username
            }
        } ]

        [ viewUser( request )( user ) {
            if( is_defined( global.users.(request.username) ) ) {
                user << global.users.(request.username)
            } else {
                throw( UserNotFound, request.username )
            }
        } ]

        [ listUsers( request )( response ) {
            i = 0
            foreach( username : global.users ) {
                user << global.users.(username)
                if( !( is_defined( request.minKarma ) && user.karma < request.minKarma ) ) {
                    response.usernames[i++] = username
                }
            }
        } ]

        [ updateUser( request )( ) {
            if( is_defined( global.users.(request.username) ) ) {
                global.users.(request.username) << request
                undef( global.users.(request.username).username )
            } else {
                throw( UserNotFound, request.username )
            }
        } ]

        [ deleteUser( request )( ) {
            if( is_defined( global.users.(request.username) ) ) {
                undef( global.users.(request.username) )
            } else {
                throw( UserNotFound, request.username )
            }
        } ]

        [ default( )( "API listens under /api/user/..." ) ]
    }
}