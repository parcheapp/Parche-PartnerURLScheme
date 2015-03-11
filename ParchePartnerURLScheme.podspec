Pod::Spec.new do |s|
  s.name         = "ParchePartnerURLScheme"
  s.version      = '1.0.0'
  s.summary      = "Parche Partner URL Scheme Helper"
  s.description  = <<-DESC
                    A utility to make it easier for partner applications of Parche to open the app using a URL scheme.
                    
                    More information 
                   DESC
  s.homepage     = "http://www.goparche.com"
  s.license      = { :type => 'Copyright', :file => 'LICENSE.md' }
  s.authors 	 = { 'Ellen Shapiro' => 'http://www.vokal.io' }
  s.source       = { :git => "https://github.com/vokal/Parche-PartnerURLScheme.git", :tag => "iOS-v#{s.version}" }
  s.source_files = "iOS/PartnerURLSchemeSample/PartnerURLSchemeHelper/*"
  s.ios.deployment_target = '7.0'
  s.requires_arc = true

end
