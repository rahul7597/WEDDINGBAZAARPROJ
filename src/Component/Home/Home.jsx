import React from 'react'
import Header from '../Main/Header/Header'
import Vendors from '../Vendors Category/Vendors'
import VendorsList from '../VendorsLists/VendorsList'

function Home() {
  return (
    <>
        <Header/>
        <Vendors/>
        <VendorsList/>
    </>
  )
}

export default Home